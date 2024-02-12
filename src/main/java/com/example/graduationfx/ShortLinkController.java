package com.example.graduationfx;
import com.example.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ShortLinkController {
    @FXML
    private Button add_button;

    @FXML
    private Label error_text;

    @FXML
    private Label link_lab;

    @FXML
    private TextField link_edit;

    @FXML
    private TextField shortlink_edit;

    @FXML
    private VBox panelVBox;

    private DB db = new DB();;

    void addButtonClick() throws SQLException, IOException, ClassNotFoundException {
        String link = link_edit.getText();
        String shortLink = shortlink_edit.getText();
        if(db.isExistsShortLink(shortLink))
            error_text.setText("такая ссылка уже есть!");
        else {
            if(link.isEmpty())
                error_text.setText("Поле для ссылки не должно быть пустым");
            if(shortLink.isEmpty())
                error_text.setText("Введите сокращенную ссылку");
            if (!link.isEmpty() && !shortLink.isEmpty()) {
                db.addLinks(link, shortLink);
                error_text.setText("");
                shortlink_edit.setText("");
                link_edit.setText("");
                initialize();
            }
        }
    }

    void deleteButton(int id) throws SQLException, ClassNotFoundException, IOException {
        db.deleteLink(id);
        initialize();
    }

    @FXML
    void initialize() throws IOException, SQLException, ClassNotFoundException {
        panelVBox.getChildren().clear();
        ResultSet res = db.getAllShortLinks();
        while (res.next()) {
            Node node = FXMLLoader.load(Objects.requireNonNull(HelloApplication.class.getResource("links.fxml")));
            Label shortLink = (Label) node.lookup("#short_link_text");
            shortLink.setText(res.getString("short_link"));

            Button buttonDel = (Button) node.lookup("#delete_button");
            int id = res.getInt("id");
            buttonDel.setOnAction(event -> {
                try {
                    deleteButton(id);
                } catch (SQLException | ClassNotFoundException | IOException e) {
                    throw new RuntimeException(e);
                }
            });

            buttonDel.setOnMouseEntered(event -> {
                buttonDel.setStyle("-fx-background-color: #ff3636");
            });

            buttonDel.setOnMouseExited(event -> {
                buttonDel.setStyle("-fx-background-color:  #fc5b5b");
            });

            String link = res.getString("link");
            node.setOnMouseEntered(event -> {
                node.setStyle("-fx-background-color: #ffeeee");
                link_lab.setText(link);
            });

            node.setOnMouseExited(event -> {
                node.setStyle("-fx-background-color: white");
                link_lab.setText("");
            });

            panelVBox.getChildren().add(node);
            panelVBox.setSpacing(5);
        }

        add_button.setOnAction(event -> {
            try {
                addButtonClick();
            } catch (SQLException | ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
            add_button.setStyle("-fx-font-size: 16px");
        });

        add_button.setOnMouseEntered(event -> {
            add_button.setStyle("-fx-background-color: #48c339");
        });

        add_button.setOnMouseExited(event -> {
            add_button.setStyle("-fx-background-color: green");
        });
    }
}