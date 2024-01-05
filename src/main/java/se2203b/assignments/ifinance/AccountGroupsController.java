package se2203b.assignments.ifinance;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.ArrayList;

public class AccountGroupsController  implements Initializable {
    int row_deleted = 100;
    private Connection conn;
    public MenuItem add_group;
    public MenuItem change_group;
    public MenuItem delete_group;
    String element_clicked;
    public Boolean adds = false;
    public Boolean edit = false;
    public Boolean delete = false;
    @FXML
    private Button save_btn;

    @FXML
    private TextField text_input;
    private AccountCategoryAdapter categoryAdapter;
    private GroupsAdapter groupsAdapter;

    private IFinanceController financeController;
    @FXML
    private TreeView<String> treeView;

    public void setModel(IFinanceController finance, GroupsAdapter groupsAdapter1) {
        this.groupsAdapter = groupsAdapter1;
        this.financeController = finance;
        populate_treeView();
    }
    public void populate_treeView() {
        try {
            String[] Category = this.categoryAdapter.all_names();
            String[] Group = this.groupsAdapter.all_names();


            TreeItem<String> rootItem = new TreeItem<>("Root");

            ObservableList<TreeItem<String>> branches = FXCollections.observableArrayList();
            for (int i = 0; i < Category.length; i++) {
                if(Category[i] == null){
                    continue;
                }
                branches.add(new TreeItem<>(Category[i]));
                rootItem.getChildren().addAll(branches.get(i));
            }

            ObservableList<TreeItem<String>> children = FXCollections.observableArrayList();
            for (int i=0;i<Group.length;i++){
                if(Group[i] == null){
                    continue;
                }
                else if (this.groupsAdapter.get_Parent(Group[i]) > row_deleted && delete){
                    try {
                        children.add(new TreeItem<>(Group[i]));
                        children.get(this.groupsAdapter.get_Parent(Group[i])).getChildren().add(children.get(i));
                    }catch (Exception e) {
                        children.add(new TreeItem<>(Group[i]));
                        children.get(this.groupsAdapter.get_Parent(Group[i])-1).getChildren().add(children.get(i));
                    }
                }
                else if(this.groupsAdapter.get_Parent(Group[i]) == 0){
                    children.add(new TreeItem<>(Group[i]));
                    branches.get(get_parent_id(this.groupsAdapter.get_element(Group[i]))).getChildren().add(children.get(i));
                }
                else {
                    children.add(new TreeItem<>(Group[i]));
                    children.get(this.groupsAdapter.get_Parent(Group[i])-1).getChildren().add(children.get(i));
                }
            }
            treeView.setShowRoot(false);
            treeView.setRoot(rootItem);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public int get_parent_id(String name){
        if(name.equals("Assets")){
            return 0;
        }
        if (name.equals("Liabilities")){
            return 1;
        }
        if (name.equals("Income")){
            return 2;
        }
        else {
            return 3;
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
            text_input.setDisable(true);
            save_btn.setDisable(true);
            add_group.setDisable(false);
            change_group.setDisable(false);
            delete_group.setDisable(false);
        try {
            // Create a named constant for the URL
            // NOTE: This value is specific for Java DB
            String DB_URL = "jdbc:derby:iFinanceDB;create=true";
            // Create a connection to the database
            conn = DriverManager.getConnection(DB_URL);
            // Create the admin account if it is not already in the database
            categoryAdapter = new AccountCategoryAdapter(conn, false);

        } catch (SQLException ex) {
            this.financeController.displayAlert(ex.getMessage());
        }
    }

    @FXML
    void close() {
        // Get current stage reference
        Stage stage = (Stage) save_btn.getScene().getWindow();
        // Close stage
        stage.close();
    }
    @FXML
    void change_clicked() {
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            element_clicked = item.getValue();
            text_input.setDisable(false);
            text_input.requestFocus();
            text_input.setText(element_clicked);
            save_btn.setDisable(false);
            edit = true;
        }
    }
    @FXML
    void save() throws Exception {
        if (edit) {
            this.groupsAdapter.edit_name(element_clicked, text_input.getText());
            populate_treeView();
            text_input.clear();
            text_input.setDisable(true);
            save_btn.setDisable(true);
            edit = false;
        }
        if (adds){
            this.groupsAdapter.delete_row("");
            this.groupsAdapter.add_row(element_clicked, text_input.getText());
            populate_treeView();
            this.groupsAdapter.delete_row("");
            text_input.clear();
            text_input.setDisable(true);
            save_btn.setDisable(true);
            adds = false;
        }
    }

    @FXML
    void check_menu() throws SQLException {
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            element_clicked = item.getValue();
            String[] Category = this.categoryAdapter.all_names();
            for (int i=0;i<Category.length;i++){
                if(Category[i] == null){
                    continue;
                }
                if (Category[i].equals(element_clicked)){
                    delete_group.setDisable(true);
                    change_group.setDisable(true);
                    return;
                }
            }
            if(this.groupsAdapter.in_DB(element_clicked)) {
                change_group.setDisable(false);
                delete_group.setDisable(this.groupsAdapter.is_parent(element_clicked));
            }
        }
    }

    public void delete_clicked() throws Exception {
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            delete = true;
            element_clicked = item.getValue();
            row_deleted = this.groupsAdapter.get_ID_name(element_clicked);
            this.groupsAdapter.delete_row(element_clicked);
            this.groupsAdapter.refactor_table();
            populate_treeView();
            delete = false;
        }
    }

    public void add_clicked() {
        text_input.setDisable(false);
        text_input.requestFocus();
        save_btn.setDisable(false);
        adds = true;
        TreeItem<String> item = treeView.getSelectionModel().getSelectedItem();
        if (item != null) {
            element_clicked = item.getValue();
        }
    }
}