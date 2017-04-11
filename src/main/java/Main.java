import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

public class Main extends Application {


    LinkedList<Badge> badges = new LinkedList<>();
    BufferedImage photo;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Бейджо-генератор 3000");
        Pane p = new Pane();

        BackgroundImage bgimg = new BackgroundImage(new Image("/regionbg.png", true), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background background = new Background(bgimg);
        p.setBackground(background);
        Button b = new Button("Выбрать");
        b.setAlignment(Pos.CENTER);
        b.relocate(750, 348);
        b.setPrefSize(150, 50);
        String[] regions = {
                "Республика Адыгея",
                "Республика Башкортостан",
                "Республика Бурятия",
                "Республика Алтай",
                "Республика Дагестан",
                "Республика Ингушетия",
                "Кабардино-Балкарская Республика",
                "Республика Калмыкия",
                "Республика Карачаево-Черкесия",
                "Республика Карелия",
                "Республика Коми",
                "Республика Марий Эл",
                "Республика Мордовия",
                "Республика Саха (Якутия)",
                "Республика Северная Осетия",
                "Республика Татарстан",
                "Республика Тыва",
                "Удмуртская Республика",
                "Республика Хакасия",
                "Чувашская Республика",
                "Алтайский край",
                "Краснодарский край",
                "Красноярский край",
                "Приморский край",
                "Ставропольский край",
                "Хабаровский край",
                "Амурская область",
                "Архангельская область",
                "Астраханская область",
                "Белгородская область",
                "Брянская область",
                "Владимирская область",
                "Волгоградская область",
                "Вологодская область",
                "Воронежская область",
                "Ивановская область",
                "Иркутская область",
                "Калининградская область",
                "Калужская область",
                "Камчатский край",
                "Кемеровская область",
                "Кировская область",
                "Костромская область",
                "Курганская область",
                "Курская область",
                "Ленинградская область",
                "Липецкая область",
                "Магаданская область",
                "Московская область",
                "Мурманская область",
                "Нижегородская область",
                "Новгородская область",
                "Новосибирская область",
                "Омская область",
                "Оренбургская область",
                "Орловская область",
                "Пензенская область",
                "Пермский край",
                "Псковская область",
                "Ростовская область",
                "Рязанская область",
                "Самарская область",
                "Саратовская область",
                "Сахалинская область",
                "Свердловская область",
                "Смоленская область",
                "Тамбовская область",
                "Тверская область",
                "Томская область",
                "Тульская область",
                "Тюменская область",
                "Ульяновская область",
                "Челябинская область",
                "Забайкальский край",
                "Ярославская область",
                "Москва",
                "Санкт-Петербург",
                "Еврейская автономная область",
                "Республика Крым",
                "Ненецкий автономный округ",
                "Ханты-Мансийский автономный округ Югра",
                "Чукотский автономный округ",
                "Ямало-Ненецкий автономный округ",
                "Севастополь",
                "Байконур",
                "Чеченская республика"};
        Arrays.sort(regions);
        ChoiceBox<String> cb = new ChoiceBox<String>();
        for (String str: regions) {
            cb.getItems().add(str);
        }
        cb.setPrefSize(450, 50);
        cb.relocate(275, 348);
        cb.getStyle();



        b.setOnAction(actionEvent -> {
            new BageCreator(cb.getValue());
            primaryStage.close();
        });

        p.getChildren().addAll(b, cb);
        p.setPrefSize(1000, 700);
        Scene scene = new Scene(p, 1000, 700);
        scene.getStylesheets().add("/styles.css");
        GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        try {
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream("/font.ttf")));
        } catch (FontFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {

        launch(args);
    }

}
