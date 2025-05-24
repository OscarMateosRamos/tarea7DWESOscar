package com.oscar.vivero.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oscar.vivero.modelo.LineaLote;
import com.oscar.vivero.repository.LineasLoteRepository;

@Service
public class ServiciosLineasLote {

	@Autowired
	LineasLoteRepository lineasloteRepo;

	public void insertarlinealote(LineaLote lt) {
		lineasloteRepo.saveAndFlush(lt);
	}
}
