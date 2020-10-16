package com.sistema.ventas.bo;

import java.util.List;

import com.sistema.ventas.exceptions.BOException;
import com.sistema.ventas.model.Genero;

public interface IGeneroBO {

	public List<Genero> findAll() throws BOException;
}
