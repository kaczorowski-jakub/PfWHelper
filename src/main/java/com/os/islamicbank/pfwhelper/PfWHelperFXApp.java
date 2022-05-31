package com.os.islamicbank.pfwhelper;

import com.os.islamicbank.pfwhelper.app.ControllerMain;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class PfWHelperFXApp extends Application {

    private ConfigurableApplicationContext context;

    @Override
    public void init() throws Exception {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.context = new SpringApplicationBuilder()
                .sources(PfWHelperApp.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        FxWeaver fxWeaver = context.getBean(FxWeaver.class);
        Parent root = fxWeaver.loadView(ControllerMain.class);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        this.context.close();
        Platform.exit();
    }
}
