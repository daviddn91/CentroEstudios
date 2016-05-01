package com.example.david.centroestudios;

/**
 * Clase para el contenido de las cardview de FAQs
 */
public class Faqs {
    private String pregunta;
    private String respuesta;

    public Faqs(String pregunta, String respuesta) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
    }

    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

}
