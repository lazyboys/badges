import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;


/**
 * Load image, provide rectangle for rubberband selection. Press right mouse button for "crop" context menu which then crops the image at the selection rectangle and saves it as jpg.
 */
public class ImageCrop {

    RubberBandSelection rubberBandSelection;
    ImageView imageView;
    Image image;

    Stage primaryStage;

    public Image getImage(){
        return image;
    }


    public ImageCrop(BageCreator bc, Image img) {

        this.primaryStage = new Stage();

        primaryStage.setTitle("Image Crop");

        BorderPane root = new BorderPane();

        // container for image layers
        ScrollPane scrollPane = new ScrollPane();

        // image layer: a group of images
        Group imageLayer = new Group();

        // the container for the image as a javafx node
        image = img;
        imageView = new ImageView( image);
        imageView.setFitHeight(700);
        imageView.setPreserveRatio(true);

        // add image to layer
        imageLayer.getChildren().add( imageView);

        // use scrollpane for image view in case the image is large
        scrollPane.setContent(imageLayer);

        // put scrollpane in scene
        root.setCenter(scrollPane);

        // rubberband selection
        rubberBandSelection = new RubberBandSelection(imageLayer);

        // create context menu and menu items
        ContextMenu contextMenu = new ContextMenu();

        MenuItem cropMenuItem = new MenuItem("Выбрать");
        cropMenuItem.setOnAction(e -> {
            // get bounds for image crop
            Bounds selectionBounds = rubberBandSelection.getBounds();
            // show bounds info
            System.out.println( "Selected area: " + selectionBounds);
            // crop the image
            crop(bc, selectionBounds);
            rubberBandSelection.removeRB();
            primaryStage.close();
        });
        contextMenu.getItems().addAll( cropMenuItem);


        // set context menu on image layer
        imageLayer.setOnMousePressed(event -> {
            contextMenu.hide();
            if (event.isSecondaryButtonDown()) {
                rubberBandSelection.removeRB();
            }
        });

        imageLayer.setOnMouseReleased(event -> {
            contextMenu.show(imageLayer, rubberBandSelection.rect.getX()+rubberBandSelection.rect.getWidth() + primaryStage.getX()- 60, rubberBandSelection.rect.getY()+rubberBandSelection.rect.getHeight() + primaryStage.getY() - 5);
        });

        primaryStage.setScene(new Scene(root, 1000, 700));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void crop(BageCreator bc, Bounds bounds) {

        int width = (int) bounds.getWidth();
        int height = (int) bounds.getHeight();

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        parameters.setViewport(new Rectangle2D( bounds.getMinX(), bounds.getMinY(), width, height));

        WritableImage wi = new WritableImage( width, height);
        imageView.snapshot(parameters, wi);
        BufferedImage bufImageARGB = SwingFXUtils.fromFXImage(wi, null);
        BufferedImage bufImageRGB = new BufferedImage(bufImageARGB.getWidth(), bufImageARGB.getHeight(), BufferedImage.OPAQUE);

        Graphics2D graphics = bufImageRGB.createGraphics();
        graphics.drawImage(bufImageARGB, 0, 0, null);
        graphics.dispose();

        image = SwingFXUtils.toFXImage(bufImageRGB, null);
        bc.updateImg(image);
    }

    /**
     * Drag rectangle with mouse cursor in order to get selection bounds
     */
    public static class RubberBandSelection {

        DragContext dragContext = new DragContext();
        Rectangle rect = new Rectangle();
        boolean haveRect = false;

        Group group;


        public Bounds getBounds() {
            return rect.getBoundsInParent();
        }

        public RubberBandSelection( Group group) {

            this.group = group;

            rect = new Rectangle( 0,0,0,0);
            rect.setStroke(Color.BLUE);
            rect.setStrokeWidth(1);
            rect.setStrokeLineCap(StrokeLineCap.ROUND);
            rect.setFill(Color.LIGHTBLUE.deriveColor(0, 1.2, 1, 0.3));

            group.addEventHandler(MouseEvent.MOUSE_PRESSED, onMousePressedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_DRAGGED, onMouseDraggedEventHandler);
            group.addEventHandler(MouseEvent.MOUSE_RELEASED, onMouseReleasedEventHandler);

        }

        EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if( event.isSecondaryButtonDown())
                    return;
                if(haveRect) {
                    if(event.getX() < rect.getX()+(rect.getWidth()/2)){
                        dragContext.mouseAnchorX = rect.getX()+rect.getWidth();
                    } else {
                        dragContext.mouseAnchorX = rect.getX();
                    }

                    if(event.getY() < rect.getY()+(rect.getHeight()/2)){
                        dragContext.mouseAnchorY = rect.getY()+rect.getHeight();
                    } else {
                        dragContext.mouseAnchorY = rect.getY();
                    }

                } else {
                    // prepare new drag operation
                    dragContext.mouseAnchorX = event.getX();
                    dragContext.mouseAnchorY = event.getY();

                    rect.setX(dragContext.mouseAnchorX);
                    rect.setY(dragContext.mouseAnchorY);
                    rect.setWidth(0);
                    rect.setHeight(0);

                    group.getChildren().add(rect);
                    haveRect = true;
                }
            }
        };

        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {

            if( event.isSecondaryButtonDown())
                return;

            double offsetX = event.getX() - dragContext.mouseAnchorX;
            double offsetY = event.getY() - dragContext.mouseAnchorY;

            if( offsetX > 0)
                rect.setWidth( offsetX);
            else {
                rect.setX(event.getX());
                rect.setWidth(dragContext.mouseAnchorX - rect.getX());
            }

            if( offsetY > 0) {
                rect.setHeight( offsetY);
            } else {
                rect.setY(event.getY());
                rect.setHeight(dragContext.mouseAnchorY - rect.getY());
            }

        };


        EventHandler<MouseEvent> onMouseReleasedEventHandler = event -> {
            if( event.isSecondaryButtonDown())
                return;

        };

        void removeRB(){
            rect.setX(0);
            rect.setY(0);
            rect.setWidth(0);
            rect.setHeight(0);

            group.getChildren().remove( rect);
            haveRect = false;
        }

        private static final class DragContext {

            public double mouseAnchorX;
            public double mouseAnchorY;


        }
    }
}