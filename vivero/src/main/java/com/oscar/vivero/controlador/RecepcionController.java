package com.oscar.vivero.controlador;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oscar.vivero.modelo.Lote;
import com.oscar.vivero.servicio.ServiciosLote;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Controller
@RequestMapping("/recepcion")
public class RecepcionController {

    @Autowired
    ServiciosLote servlote;

    @Transactional
    @GetMapping("/info")
    public String info(HttpSession session, Model model) {
        // Obtener lotes recibidos y no recibidos
        ArrayList<Lote> lotesRecibidos = servlote.buscarLotesRecibidos();
        ArrayList<Lote> lotesNoRecibidos = servlote.buscarLotesNoRecibidos();

        // Forzar la carga de las líneas para evitar errores de lazy loading
        lotesRecibidos.forEach(l -> {
            if (l.getLineasLote() != null) l.getLineasLote().size();
        });
        lotesNoRecibidos.forEach(l -> {
            if (l.getLineasLote() != null) l.getLineasLote().size();
        });

        model.addAttribute("lotesRecibidos", lotesRecibidos);
        model.addAttribute("lotesNoRecibidos", lotesNoRecibidos);

        return "personal/infolotes";
    }
}
