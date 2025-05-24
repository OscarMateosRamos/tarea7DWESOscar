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

@Controller
@RequestMapping("/recepcion")
public class RecepcionController {

	@Autowired
	ServiciosLote servlote;

	@GetMapping("/info")
	public String info(HttpSession session, Model model) {
		ArrayList<Lote> lotesRecibidos = servlote.buscarLotesRecibidos();
		ArrayList<Lote> lotesNoRecibidos = servlote.buscarLotesNoRecibidos();
		model.addAttribute("lotesRecibidos", lotesRecibidos);
		model.addAttribute("lotesNoRecibidos", lotesNoRecibidos);
		return "/personal/infolotes";
	}

}
