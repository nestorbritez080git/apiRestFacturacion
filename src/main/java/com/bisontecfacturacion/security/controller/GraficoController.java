package com.bisontecfacturacion.security.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bisontecfacturacion.security.auxiliar.Grafico;
import com.bisontecfacturacion.security.repository.VentaRepository;

@RestController
@RequestMapping("grafico")
public class GraficoController {
	
	@Autowired
	private VentaRepository entityRepository;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Grafico> getAlls(){
		List<Grafico> g1=new ArrayList<>();
		List<Grafico> g2=new ArrayList<>();
		
		for (int i = 0; i < 24; i++) {
			Grafico gr=new Grafico();
			gr.setName(i+"");
			gr.setValue(0);
			g1.add(gr);
		}
		
		
		Date fecha=new Date();
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
		String fechas=formater.format(fecha);
		String[] fec=fechas.split("-");
		Integer dia=Integer.parseInt(fec[0]);
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		
		List<Object[]> objeto = entityRepository.getMovimientoDelDia(ano, mes, dia);
		
		for (Object[] objects : objeto) {
			Grafico gr=new Grafico();
			gr.setName(objects[0].toString());
			gr.setValue(Integer.parseInt(objects[1].toString()));
			g2.add(gr);
		}
		
		for (int i = 0; i < g1.size(); i++) {
			for (int j = 0; j < g2.size(); j++) {
				int p1 = Integer.parseInt(g1.get(i).getName());
				int p2 = Integer.parseInt(g2.get(j).getName());
				
				if (p1 == p2) {
					g1.get(i).setValue(g2.get(j).getValue());
					g1.get(i).setName(g1.get(i).getName());
				}
			}
		}
		
		return g1;
	}
	
	
	
	@RequestMapping(method=RequestMethod.GET, value="/mes")
	public List<Grafico> getGraficoPorMes(){
		List<Grafico> lista =new ArrayList<Grafico>();
		List<Grafico> lista1 =new ArrayList<Grafico>();
		
		int contador=obtenerDia()+1;
		for (int i = 1; i < contador; i++) {
			Grafico g=new Grafico();
			g.setName(i+"");
			lista.add(g);
		}
		
		Date fecha=new Date();
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
		String fechas=formater.format(fecha);
		String[] fec=fechas.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		
		List<Object[]> objeto = entityRepository.getMovimientoPorMes(ano, mes);
		for (Object[] objects : objeto) {
			Grafico gr=new Grafico();
			gr.setName(objects[0].toString());
			gr.setValue(Integer.parseInt(objects[1].toString()));
			lista1.add(gr);
		}
		
		
		for (int i = 0; i < lista.size(); i++) {
			for (int j = 0; j < lista1.size(); j++) {
				Double p1=Double.parseDouble(lista.get(i).getName());
				Double p2=Double.parseDouble(lista1.get(j).getName());
				
				if (p1.equals(p2)) {
					lista.get(i).setValue(lista1.get(j).getValue());
				}
			}
		}
		
		return lista;
	}
	
	public int obtenerDia() {
		Date fecha=new Date();
		SimpleDateFormat formater=new SimpleDateFormat("dd-MM-yyyy");
		String fechas=formater.format(fecha);
		String[] fec=fechas.split("-");
		Integer mes=Integer.parseInt(fec[1]);
		Integer ano=Integer.parseInt(fec[2]);
		
		return diasDelMes(mes-1, ano);
	}
	

	public int diasDelMes(int mes, int año){
        switch(mes){
            case 0:  // Enero
            case 2:  // Marzo
            case 4:  // Mayo
            case 6:  // Julio
            case 7:  // Agosto
            case 9:  // Octubre
            case 11: // Diciembre
                return 31;
            case 3:  // Abril
            case 5:  // Junio
            case 8:  // Septiembre
            case 10: // Noviembre
                return 30;
            case 1:  // Febrero
                if ( ((año%100 == 0) && (año%400 == 0)) ||
                        ((año%100 != 0) && (año%  4 == 0))   )
                    return 29;  // Año Bisiesto
                else
                    return 28;
            default:
                throw new java.lang.IllegalArgumentException(
                "El mes debe estar entre 0 y 11");
        }
}

}
