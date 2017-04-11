import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;


public class BageCreator {

    int namex = 100, namey = 207, namew = 360, nameh = 55;                  //NAME
    int surnamex = 100, surnamey = 323, surnamew = 360, surnameh = 55;      //SURNAME
    int cbx = 100, cby = 440, cbw = 330, cbh = 55;                          //CHOICE BOX
    int picx = 600, picy = 265, picw = 305, pich = 305;                     //PICTURE
    int cropbx = 600, cropby = 208, cropbw = 305, cropbh = 50;              //CROP BUTTON
    int addbx = 350, addby = 600, addbw, addbh;                             //ADD BUTTON
    int watchbx = 550, watchby = 600, watchbw, watchbh;                     //PREVIEW BUTTON
    int donebx = 450, doneby = 650, donebw, donebh;                         //DONE BUTTON


    int sceneH = 700, sceneW = 1000;





    LinkedList<Badge> badges = new LinkedList<>();
    BufferedImage photo;
    String region;
    TextField firstName = new TextField("Фамилия"), secondName = new TextField("Имя");
    Image img = null, bg, bbg;
    Button addB = new Button("Добавить");
    Button previewB = new Button("Посмотреть");
    ChoiceBox<String> cb = new ChoiceBox(FXCollections.observableArrayList( "Участник","Организатор","Руководитель \nделегации","Волонтер","Гость", "Почетный Гость"));
    Button picB = new Button("Выбрать\n  фото...");
    Button cropB = new Button("Обрезать");
    Button doneB = new Button("Выгрузить");

    BageCreator(String region) {
        this.region = region;
        Stage stage = new Stage();
        stage.setTitle("Бейджо-генератор 3000");
        Pane p = new Pane();
        bg = new Image("/badgebg.png", true);
        bbg = new Image("/trueBadge.png", true);
        BackgroundImage bgimg = new BackgroundImage(bg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        Background bg = new Background(bgimg);
        p.setBackground(bg);
        FileChooser fileChooser = new FileChooser();


        picB.relocate(picx, picy);                                         //       ******   ******** **        ******   ******     **    ******** ********
        firstName.relocate(namex,namey);                                   //       **   **  **       **       ***  *** ***  ***   ****   ******** **
        secondName.relocate(surnamex,surnamey);                            //       **    ** **       **       **    ** **        **  **     **    **
        addB.relocate(addbx,addby);                                        //       *******  ******** **       **    ** **       **    **    **    ********
        previewB.relocate(watchbx,watchby);                                //       ** ***   **       **       **    ** **       ********    **    **
        cb.relocate(cbx, cby);                                             //       **  ***  **       **       ***  *** ***  *** **    **    **    **
        cropB.relocate(cropbx,cropby);                                     //       **   *** ******** ********  ******   ******  **    **    **    ********
        doneB.relocate(donebx, doneby);



        picB.setPrefSize(picw, pich);                                      //        ******  ******** ********  ********
        firstName.setPrefSize(namew,nameh);                                //       ***  ***    **         **   **
        secondName.setPrefSize(surnamew,surnameh);                         //       ***         **        **    **
//        addB.setPrefSize(addbw,addbh);                                   //        ******     **       **     ********
//        previewB.setPrefSize(watchbw,watchbh);                           //            ***    **      **      **
        cb.setPrefWidth(cbw);                                              //       ***  ***    **     **       **
        cropB.setPrefSize(cropbw,cropbh);                                  //        ******  ******** ********  ********
        
        
        
        
        cropB.setOnAction(actionEvent -> new ImageCrop(this, SwingFXUtils.toFXImage(photo, null)));

        picB.setOnAction(actionEvent -> {
                        try {
                            photo = ImageIO.read(fileChooser.showOpenDialog(stage));
                            img = SwingFXUtils.toFXImage(photo, null);
                            ImageView pic = new ImageView(img);
                            pic.setFitHeight(pich);
                            pic.setFitWidth(picw);
                            picB.setGraphic(pic);
                            picB.setText("");

                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
        });

        addB.setOnAction(e -> addBadge());

        previewB.setOnAction(e -> showBadge());

        doneB.setOnAction(e -> {
            new DocCreator(badges, region);
        });


        p.setPrefSize(sceneW, sceneH);
        p.getChildren().addAll(firstName,secondName,cb, addB, picB, cropB, previewB, doneB);
        Scene scene = new Scene(p, sceneW, sceneH);
        scene.getStylesheets().add("/styles.css");
        p.setId("my");
        stage.setScene(scene);
        //stage.setResizable(false);
        stage.show();
        System.out.print(firstName.getWidth() + "    " + firstName.getHeight() + "\n");
    }

    void addBadge(){
        String name;
        name = firstName.getText() + "\n" + secondName.getText();
        try {
            badges.add(new Badge(firstName.getText(), secondName.getText(), cb.getValue(), region, SwingFXUtils.fromFXImage(img, null),SwingFXUtils.fromFXImage(bbg, null)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File dir = new File("badges");
        dir.mkdir();
        File f = new File("badges\\badge"+badges.size()+".png");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print(f.getAbsolutePath()+"\n");
        try {
            ImageIO.write(badges.getLast().getBadge(), "png", f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void showBadge(){
        ImageView pic = new ImageView(SwingFXUtils.toFXImage(badges.getLast().getBadge(), null));
        pic.setFitHeight(700);
        pic.setPreserveRatio(true);
        Label img = new Label();
        img.setGraphic(pic);
        img.setAlignment(Pos.CENTER);
        Scene scene = new Scene(img, pic.getFitWidth(), pic.getFitHeight());
        Stage newStage = new Stage();
        newStage.setTitle("Предпросмотр");
        newStage.setResizable(false);
        newStage.setScene(scene);
        newStage.show();

    }

    void updateImg(Image img) {
        ImageView iv = new ImageView(img);
//        if(img.getWidth() > img.getHeight()) {
//            iv.setFitWidth(picw);
//        } else {
//            iv.setFitHeight(pich);
//        }
//        iv.setPreserveRatio(true);
        iv.setFitHeight(300);
        iv.setFitWidth(200);
        picB.setGraphic(iv);
        this.img = img;
    }
}
