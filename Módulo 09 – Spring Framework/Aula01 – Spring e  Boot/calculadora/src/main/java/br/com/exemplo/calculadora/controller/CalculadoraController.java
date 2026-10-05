package br.com.exemplo.calculadora.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CalculadoraController {

    @GetMapping("/soma/{numero1}/{numero2}")
    public String soma(@PathVariable int numero1, @PathVariable int numero2) {
        int resultado = numero1 + numero2;
        return "Resultado: " + resultado;
    }

    @GetMapping("/subtracao/{numero1}/{numero2}")
    public String subtracao(@PathVariable int numero1, @PathVariable int numero2) {
        int resultado = numero1 - numero2;
        return "Resultado: " + resultado;
    }

    @GetMapping("/multiplicacao/{numero1}/{numero2}")
    public String multiplicacao(@PathVariable int numero1, @PathVariable int numero2) {
        int resultado = numero1 * numero2;
        return "Resultado: " + resultado;
    }

    @GetMapping("/divisao/{numero1}/{numero2}")
    public String divisao(@PathVariable int numero1, @PathVariable int numero2) {
        if (numero2 == 0) {
            return "Erro: divisão por zero não é permitida";
        }
        double resultado = (double) numero1 / numero2;
        return "Resultado: " + resultado;
    }

}
