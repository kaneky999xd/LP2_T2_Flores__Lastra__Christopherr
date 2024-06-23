package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.AreaEntity;
import com.example.demo.entity.EmpleadoEntity;
import com.example.demo.repository.AreaRepository;
import com.example.demo.repository.EmpleadoRepository;

@Controller
public class EmpleadosController {

    @Autowired
    private EmpleadoRepository empleadoRepository;
    
    @Autowired
    private AreaRepository areaRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<EmpleadoEntity> listaEmpleado = empleadoRepository.findAll();
        model.addAttribute("lst_empleados", listaEmpleado);
        return "home";
    }

    @GetMapping("/create")
    public String createEmpleadoForm(Model model) {
        List<AreaEntity> listaAreas = areaRepository.findAll();
        model.addAttribute("lista_areas", listaAreas);
        model.addAttribute("empleado", new EmpleadoEntity());
        return "create";
    }

    @PostMapping("/create")
    public String createEmpleado(@ModelAttribute EmpleadoEntity empleado, Model model) {
        try {
            if (empleadoRepository.findByDniEmpleado(empleado.getDniEmpleado()) != null) {
                model.addAttribute("errorMessage", "El empleado con este DNI ya existe");
                model.addAttribute("empleado", new EmpleadoEntity());
                List<AreaEntity> listaAreas = areaRepository.findAll();
                model.addAttribute("lista_areas", listaAreas);
                return "create";
            }
            empleadoRepository.save(empleado);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error al guardar el empleado: " + e.getMessage());
            model.addAttribute("empleado", new EmpleadoEntity());
            return "create";
        }
    }

    @GetMapping("/update/{id}")
    public String ViewActualizar(Model model, @PathVariable("id") String id) {
        EmpleadoEntity empleado = empleadoRepository.findById(id).orElse(null);
        if (empleado == null) {
            return "redirect:/";
        }
        List<AreaEntity> listaAreas = areaRepository.findAll();
        model.addAttribute("lista_areas", listaAreas);
        model.addAttribute("empleado", empleado);
        return "update";
    }

    @PostMapping("/update")
    public String UpdateEmpleado(@ModelAttribute EmpleadoEntity empleado, Model model) {
        empleadoRepository.save(empleado);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String eliminarEmpleado(@PathVariable("id") String id) {
        empleadoRepository.deleteById(id);
        return "redirect:/";
    }

	  // Método para buscar empleado por DNI
    @GetMapping("/empleado")
    public String buscarEmpleadoPorDni(@RequestParam("id") String dniBuscado, Model model) {
        EmpleadoEntity empleado = empleadoRepository.findByDniEmpleado(dniBuscado);
        if (empleado != null) {
            List<EmpleadoEntity> listaEmpleado = new ArrayList<>();
            listaEmpleado.add(empleado);
            model.addAttribute("lst_empleados", listaEmpleado);
            model.addAttribute("dniBuscado", dniBuscado);
        } else {
            model.addAttribute("errorMessage", "Empleado no encontrado con DNI: " + dniBuscado);
            List<EmpleadoEntity> listaEmpleado = empleadoRepository.findAll();
            model.addAttribute("lst_empleados", listaEmpleado);
        }
        return "home";
    }
}