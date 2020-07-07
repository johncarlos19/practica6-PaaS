package controladorServer;

import io.javalin.Javalin;


public abstract class JavalinControlador {

    protected Javalin app;

    public JavalinControlador(Javalin app) {
        this.app = app;
    }

    abstract public void aplicarRutas();

}
