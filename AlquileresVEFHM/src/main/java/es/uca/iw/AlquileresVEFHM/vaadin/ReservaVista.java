package es.uca.iw.AlquileresVEFHM.vaadin;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

import es.uca.iw.AlquileresVEFHM.DAO.ApartamentoDAO;
import es.uca.iw.AlquileresVEFHM.DAO.OfertaDAO;
import es.uca.iw.AlquileresVEFHM.modelos.Apartamento;
import es.uca.iw.AlquileresVEFHM.modelos.Oferta;

@SpringView(name = ReservaVista.NOMBRE)
public class ReservaVista extends VerticalLayout implements View {
	public final static String NOMBRE = "reserva";
	
	private Apartamento apartamento;
	
	private final ApartamentoDAO aparDao;
	private final OfertaDAO ofertaDao;
	
	@Autowired
	public ReservaVista(ApartamentoDAO ad, OfertaDAO od) {
		aparDao = ad;
		ofertaDao = od;
	}
	
	@PostConstruct
	void init() {
		setSizeFull();
	}
	
	@Override
	public void enter(ViewChangeListener.ViewChangeEvent e) {
		apartamento = aparDao.findById(Integer.parseInt(e.getParameters())).get();
		
		LocalDate inicio = LocalDate.now().withDayOfMonth(1);
		LocalDate fin = inicio.withDayOfMonth(inicio.lengthOfMonth());
		
		Set<Oferta> ofertasmes = ofertaDao.ofertaIntervalo(apartamento.getId(), inicio, fin);
		System.out.println(ofertasmes);
		Oferta oferta;
		if(ofertasmes.iterator().hasNext()) oferta = ofertasmes.iterator().next();
		else {
			oferta = new Oferta();
			oferta.setLDFecha(LocalDate.MIN);
		}
		
		Panel calendario = new Panel(inicio.getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault()));
		
		VerticalLayout vl = new VerticalLayout();
		int diasem = 1;
		HorizontalLayout hl = new HorizontalLayout();
		for(LocalDate i = inicio; i.compareTo(fin) <= 0; i = i.plusDays(1)) {
			System.out.println(i + "    " + oferta.getLDFecha());
			if(diasem == 8) {
				vl.addComponent(hl);
				hl = new HorizontalLayout();
				diasem = 1;
			}
			if(i.compareTo(inicio) == 0) {
				LocalDate premes = inicio.minusDays(inicio.getDayOfWeek().getValue()-1);
				while(premes.compareTo(inicio) != 0) {
					Panel dia = new Panel();
					dia.setWidth(130.0f, Unit.PIXELS);
					dia.setHeight(130.0f, Unit.PIXELS);
					dia.setCaption(Integer.toString(premes.getDayOfMonth()));
					dia.setContent(new VerticalLayout());
					hl.addComponent(dia);
					premes = premes.plusDays(1);
					++diasem;
				}
			}
			Panel dia = new Panel();
			dia.setWidth(130.0f, Unit.PIXELS);
			dia.setHeight(130.0f, Unit.PIXELS);
			dia.setCaption(Integer.toString(i.getDayOfMonth()));
			Button p = new Button();
			p.addStyleName(ValoTheme.BUTTON_TINY);
			if(oferta.getLDFecha().compareTo(i) == 0) {
				if(oferta.getReserva() == null) {
					Oferta ofer = oferta;
					p.addStyleName(ValoTheme.BUTTON_PRIMARY);
					String caption = "Disponible " + ofer.getPrecio() + "€";
					p.addClickListener(new ClickListener() {
						@Override
						public void buttonClick(ClickEvent event) {
							if(p.getCaption().equals(caption)) {
								p.removeStyleName(ValoTheme.BUTTON_PRIMARY);
								p.addStyleName(ValoTheme.BUTTON_FRIENDLY);
								p.setCaption("Seleccionado");
							}else {
								p.removeStyleName(ValoTheme.BUTTON_FRIENDLY);
								p.addStyleName(ValoTheme.BUTTON_PRIMARY);
								p.setCaption(caption);
							}
						}
					});
					p.setCaption(caption);
				}else {
					p.addStyleName(ValoTheme.BUTTON_DANGER);
					p.setCaption("Reservado");
					p.setEnabled(false);
				}
				
				if(ofertasmes.iterator().hasNext()) ofertasmes.remove(ofertasmes.iterator().next());
				if(ofertasmes.iterator().hasNext()) oferta = ofertasmes.iterator().next();
			} else {
				p.setCaption("No disponible");
				p.setEnabled(false);
			}
			p.setSizeFull();
			dia.setContent(p);
			hl.addComponent(dia);
			++diasem;
		}
		while(diasem != 8) {
			Panel dia = new Panel();
			dia.setWidth(130.0f, Unit.PIXELS);
			dia.setHeight(130.0f, Unit.PIXELS);
			dia.setCaption(Integer.toString(fin.getDayOfMonth()));
			dia.setContent(new VerticalLayout());
			hl.addComponent(dia);
			fin = fin.plusDays(1);
			++diasem;
		}
		vl.addComponent(hl);
		calendario.setContent(vl);
		addComponent(calendario);
	}
}

