/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ci5105202122k2056101.eventmanager.view;

import ci5105202122k2056101.eventmanager.control.GUIControl;
import ci5105202122k2056101.eventmanager.model.Event;
import ci5105202122k2056101.eventmanager.model.Eventmanager;
import ci5105202122k2056101.eventmanager.model.Item;
import ci5105202122k2056101.eventmanager.model.Organiser;
import ci5105202122k2056101.eventmanager.utils.DataManager;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.*;

/**
 *
 * @author k2056101
 */
public class GuiViewer extends JFrame {

    // private JTextArea textArea = new JTextArea();
    private JMenuBar menubar;
    private static GuiViewer start;
    private static JDialog viewEventWindow;
    private static ScrollPane mainPane = new ScrollPane();//Main scrollable area
    private static GUIControl controls;

    /**
     * Start point set DataManager.setEventManager(new Eventmanager()); if not
     * done in main method and only using gui
     *
     */
    public static void GuiViewerStart() { //Start point of gui
        DataManager.setEventManager(new Eventmanager());// Comment out if using test console -- WILL REMOVE ALL DATA
        start = new GuiViewer(); // New gui viewer
        start.setLayout(new BorderLayout()); // New gui viewer      
        controls = new GUIControl();//New gui control element
        controls.actionForButtons(); // Assign action listener
        start.viewMenuBar();// Jmenubar add to view
        start.viewMenuBar();
        mainPane.removeAll();
        JButton add = new JButton("Add Event");
        add.addActionListener(controls);
        start.add(add, BorderLayout.NORTH);
        JButton addItem = new JButton("Add Item");
        addItem.addActionListener(controls);
        start.add(addItem, BorderLayout.SOUTH);
        GuiViewer.updateView();
        start.view();
        //make frame visible

    }

    public void view() { // Make viewer visible and size 500 -500
        this.setSize(500, 500);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(rootPaneCheckingEnabled);
    }

    //JFrame-->mainPane-->eventList-->buttonPanel,view,textarea
    public static void updateView() {
        JPanel eventList = new JPanel(new GridLayout(0, 1));// List of events
        //eventList.setPreferredSize(new Dimension(450, 100));
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1));// Panel for east side buttons
        //BoxLayout box = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        JPanel eventPanel = new JPanel(new BorderLayout()); // // Panel new 
        //eventPanel.setPreferredSize(new Dimension(1000, 1000));
        JPanel eventData = new JPanel(new GridLayout(0, 1)); // // Panel new  
        JPanel viewButton = new JPanel(new GridLayout(0, 1)); // // Panel new  

        int en = 0;// For button action
        int in = 0;

        for (Item item : DataManager.getEventManager().getItemList()) {
            viewButton.add(new JLabel("Item"));
            JTextArea text = new JTextArea(DataManager.listItem(item));
            text.setPreferredSize(new Dimension(300, 100));
            eventData.add(text);
            eventPanel.add(viewButton, BorderLayout.WEST);
            eventPanel.add(eventData, BorderLayout.CENTER);
            eventPanel.add(buttonPanel, BorderLayout.EAST);

            JButton edit = new JButton("Edit");
            edit.addActionListener(controls);
            edit.setActionCommand("EdItem" + in);
            buttonPanel.add(edit);

            JButton delete = new JButton("Delete");
            delete.addActionListener(controls);
            delete.setActionCommand("DelItem" + in);
            buttonPanel.add(delete);

            in++;
            eventList.add(eventPanel);
        }

        for (Event event : DataManager.getEventManager().getEventList()) {//    LOOP
            JButton view = new JButton("View");
            view.setActionCommand("View" + en);
            view.addActionListener(controls);
            viewButton.add(view);

            JTextArea text = new JTextArea(DataManager.listEvent(event));
            text.setPreferredSize(new Dimension(300, 100));
            eventData.add(text);
            eventPanel.add(viewButton, BorderLayout.WEST);
            eventPanel.add(eventData, BorderLayout.CENTER);
            eventPanel.add(buttonPanel, BorderLayout.EAST);

            JButton edit = new JButton("Edit");
            edit.addActionListener(controls);
            edit.setActionCommand("Edit" + en);
            buttonPanel.add(edit);

            JButton delete = new JButton("Delete");
            delete.addActionListener(controls);
            delete.setActionCommand("Delete" + en);
            buttonPanel.add(delete);
            en++;
            eventList.add(eventPanel);
        }

        System.out.println(en + " Times looped"); // niber counter XXXXXXXX REMOVE LATER

        //Main elements and containers
        //Adding the panelcreated
        mainPane.add(eventList);//Add the list at the end to the scroll pane
        start.add(mainPane, BorderLayout.CENTER);// For updating its at the end
        SwingUtilities.updateComponentTreeUI(start); // uPDATE WINDOW
        //start.update(start.getGraphics());// ----Works but slow

    }

    public static void viewEvent(Event event) {
        // addWindow --> scrollPane --> firstPanel --> eventPanel,itemsPanel -->
        // eventPanel-->listEvent
        // itemsPanel-->buttonPanel,ItemPanel
        if (viewEventWindow != null) {
            viewEventWindow.dispose(); // -- closing opened windows, needed to compensate design flaw
        }
        viewEventWindow = new JDialog(start, event.getTitle());
        viewEventWindow.setLayout(new BorderLayout());
        ScrollPane scrollPane = new ScrollPane();
        viewEventWindow.add(scrollPane);
        JPanel firstPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel itemsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JPanel eventPanel = new JPanel(new BorderLayout());
        scrollPane.add(firstPanel);
        firstPanel.add(eventPanel);
        firstPanel.add(itemsPanel);

        ActionListener window;
        window = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Add Event Item")) {
                    System.out.println("Clicked add Event item");
                    GuiViewer.addItems(event);
                    GuiViewer.updateView();
                    // - TESTING --//
                    //viewEventWindow.getOwner().dispose();
                    System.out.println(viewEventWindow);

                } else if (e.getActionCommand().equals("setOrganiser")) {
                    System.out.println("Clicked set organiser");
                    GuiViewer.editOrganiser(event);
                } else if (e.getActionCommand().contains("editItem")) {
                    int n = Integer.valueOf(e.getActionCommand().replace("editItem", ""));
                    System.out.println("Clicked edit event item " + n);
                    GuiViewer.editItems(event.getAgendaItem().get(n));
                    //viewEventWindow.dispose();

                    // GuiViewer.viewEvent(event);
                } else if (e.getActionCommand().contains("deleteItem")) {
                    int n = Integer.valueOf(e.getActionCommand().replace("deleteItem", ""));
                    System.out.println("Clicked set delete event item " + n);
                    event.getAgendaItem().remove(n);
                    //viewEventWindow.dispose(); //-- needed if not disposed in the following method
                    GuiViewer.viewEvent(event); // SUITABLE AS DELTETE IS INSTANT
                    GuiViewer.updateView(); // SUITABLE AS DELTETE IS INSTANT

                } else if (e.getActionCommand().equals("Cancel")) {
                    viewEventWindow.dispose();
                }
            }
        };
        // -- Add Event Item -- //
        JButton addItem = new JButton("Add Item");
        addItem.setActionCommand("Add Event Item");
        addItem.addActionListener(window);
        viewEventWindow.add(addItem, BorderLayout.NORTH);
        // -- Organiser setting -- //
        JButton setOrganiser = new JButton("Change/Set " + System.lineSeparator() + " Organiser");
        setOrganiser.setActionCommand("setOrganiser");
        setOrganiser.addActionListener(window);

// -- EVENT DATA --//
        JTextArea text = new JTextArea(DataManager.listEvent(event));

        eventPanel.add(text, BorderLayout.CENTER);
        eventPanel.add(setOrganiser, BorderLayout.EAST);

        //--ITEM DATA--//
        int in = 0; //item number

        // - LOOP - //
        for (Item item : event.getAgendaItem()) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            JPanel buttonPanel = new JPanel(new GridLayout(2, 0, 5, 5));
            itemsPanel.add(itemPanel);
            JButton editItem = new JButton("Edit");
            editItem.setActionCommand("editItem" + in);
            editItem.addActionListener(window);
            JButton deleteItem = new JButton("Delete");
            deleteItem.setActionCommand("deleteItem" + in);
            deleteItem.addActionListener(window);
            buttonPanel.add(editItem);
            buttonPanel.add(deleteItem);
            itemPanel.add(buttonPanel, BorderLayout.EAST);
            JTextArea itemText = new JTextArea(DataManager.listItem(item));
            itemPanel.add(itemText);
            in++;
        }

        // -- SET TO LAST LINES -- //
        viewEventWindow.setSize(500, 500);
        viewEventWindow.setVisible(true);
        viewEventWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);

    }

    public static void addEventForm() {
        JDialog addWindow = new JDialog();
        addWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        addWindow.add(form);

        JLabel time = new JLabel();
        JLabel error = new JLabel();
        error.setForeground(Color.red);
        time.setForeground(Color.red);

        JTextField Time = new JTextField("00:00", 15);
        JTextField Date = new JTextField("2000-01-01", 15);
        JTextField Location = new JTextField("Not set", 15);
        JTextField EventTitle = new JTextField("Not set", 15);

        form.add(error);
        form.add(time);

        form.add(new JLabel("Event Title"));
        form.add(EventTitle);
        form.add(new JLabel("Event Time HH:FF Fornat"));
        form.add(Time);
        form.add(new JLabel("Event Date YYYY-MM-DD Format"));
        form.add(Date);
        form.add(new JLabel("Event Location"));
        form.add(Location);

        ActionListener window;
        window = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Add")) {
                    //       DataManager.getEventManager().addEventToManager(new Event(EventTitle.getText(), Time.getText(), Date.getText(), Location.getText()));
                    try {
                        DataManager.getEventManager().addEventToManager(new Event(EventTitle.getText(), Time.getText(), Date.getText(), Location.getText()));
                        GuiViewer.updateView();
                        addWindow.dispose();
                    } catch (DateTimeParseException exeption) {
                        System.out.println("Date and time format is wrong");
                        time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                        error.setText("   DateTimeFormat error");
                    } catch (Exception allexeption) {
                        System.out.println("Something went wrong");
                        error.setText("Something went wrong");
                    }

                } else if (e.getActionCommand().equals("Cancel")) {
                    addWindow.dispose();
                }
            }
        };

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(window);
        form.add(cancel);
        JButton add = new JButton("Add");//Added button add
        add.addActionListener(window);
        form.add(add);

        addWindow.setSize(400, 400);
        addWindow.setVisible(true);
    }

    public static void editEventForm(Event event) {
        JDialog addWindow = new JDialog();
        addWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        addWindow.add(form);

        JLabel time = new JLabel();
        JLabel error = new JLabel();
        error.setForeground(Color.red);
        time.setForeground(Color.red);

        JTextField Time = new JTextField(event.getTime().format(DateTimeFormatter.ofPattern("hh:mm")), 15);
        JTextField Date = new JTextField(event.getDate().format(DateTimeFormatter.ISO_DATE));
        JTextField Location = new JTextField(event.getLocation(), 15);
        JTextField EventTitle = new JTextField(event.getTitle(), 15);

        form.add(error);
        form.add(time);

        form.add(new JLabel("Event Title"));
        form.add(EventTitle);
        form.add(new JLabel("Event Time HH:FF Fornat"));
        form.add(Time);
        form.add(new JLabel("Event Date YYYY-MM-DD Format"));
        form.add(Date);
        form.add(new JLabel("Event Location"));
        form.add(Location);

        ActionListener window;
        window = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Save")) {
                    // DataManager.getEventManager().addEventToManager(new Event(EventTitle.getText(), Time.getText(), Date.getText(), Location.getText()));

                    try {
                        event.setTitle(EventTitle.getText());
                        event.setTime(Time.getText());
                        event.setLocation(Location.getText());
                        event.setDate(Date.getText());
                        GuiViewer.updateView();
                        addWindow.dispose();

                    } catch (DateTimeParseException exeption) {
                        System.out.println("Date and time format is wrong");
                        time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                        error.setText("   DateTimeFormat error");
                    } catch (Exception allexeption) {
                        System.out.println("Something went wrong");
                        error.setText("Something went wrong");
                    }

                } else if (e.getActionCommand().equals("Cancel")) {
                    addWindow.dispose();
                }
            }
        };

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(window);
        form.add(cancel);
        JButton save = new JButton("Save");//Added button add
        save.addActionListener(window);
        form.add(save);

        addWindow.setSize(400, 400);
        addWindow.setVisible(true);
    }

    public static void editOrganiser(Event event) {
        JDialog addWindow = new JDialog();
        addWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 50));
        addWindow.add(form);

        JTextField name = new JTextField(event.getOrganiser().getName(), 15);
        form.add(new JLabel("   Organiser name"));
        form.add(name);

        ActionListener window;
        window = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Save")) {

                    event.setOrganiser(new Organiser(name.getText()));
                    //viewEventWindow.dispose(); //-- needed if not disposed in the following method
                    GuiViewer.viewEvent(event);
                    GuiViewer.updateView();
                    addWindow.dispose();
                } else if (e.getActionCommand().equals("Cancel")) {
                    addWindow.dispose();
                }
            }
        };

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(window);
        form.add(cancel);
        JButton save = new JButton("Save");//Added button add
        save.addActionListener(window);
        form.add(save);

        addWindow.setSize(400, 200);
        addWindow.setVisible(true);
    }

    public static void viewItem(Item item) {

        JDialog viewWindow = new JDialog();
        viewWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        viewWindow.add(form);
        JTextArea text = new JTextArea(DataManager.listItem(item));;
        text.setPreferredSize(new Dimension(400, 100));
        form.add(text);

        viewWindow.setSize(400, 400);
        viewWindow.setVisible(true);

    }

    /**
     * For both event item and General manager items
     *
     * @param object
     */
    public static void addItems(Object object) {
        JDialog addWindow = new JDialog(start, "Adding item ");
        addWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        addWindow.add(form);

        JLabel time = new JLabel();
        JLabel error = new JLabel();
        error.setForeground(Color.red);
        time.setForeground(Color.red);

        JTextField Time = new JTextField("00:00", 15);
        JTextField ItemTitle = new JTextField("Not set", 15);

        form.add(error);
        form.add(time);

        form.add(new JLabel("Item Title"));
        form.add(ItemTitle);
        form.add(new JLabel("Event Time start HH:FF Fornat"));
        form.add(Time);

        ActionListener window;
        window = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Add")) {

                    try {

                        if (object.getClass() == DataManager.getEventManager().getClass()) {
                            DataManager.getEventManager().addItemToManager(
                                    new Item(ItemTitle.getText(), Time.getText()));
                            System.out.println("Recognised manager add item added");
                        } else if (DataManager.getEventManager().getEventList().contains(object)) {
                            System.out.println("Recognised event add item trigger");

                            Event event = (Event) object;
                            event.addIAgendatemToEvent(new Item(ItemTitle.getText(), Time.getText()));
                            // IMPLEMENT VIEW EVENT

                            //addWindow.getOwner().getOwnedWindows()[0].dispose(); KIND OF WORKED
                            //viewEventWindow.dispose(); //-- needed if not disposed in the following method
                            GuiViewer.viewEvent((Event) object);
                        }
                        addWindow.dispose();
                        GuiViewer.updateView(); // Updates main window - boe both add event item and normal item

                    } catch (DateTimeParseException exeption) {
                        System.out.println("Date and time format is wrong");
                        time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                        error.setText("   DateTimeFormat error");
                    } catch (Exception allexeption) {
                        System.out.println("Something went wrong");
                        error.setText("Something went wrong");
                    }

                } else if (e.getActionCommand().equals("Cancel")) {
                    addWindow.dispose();
                }
            }
        };

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(window);
        form.add(cancel);
        JButton save = new JButton("Add");//Added button add
        save.addActionListener(window);
        form.add(save);

        addWindow.setSize(400, 400);
        addWindow.setVisible(true);
    }

    public static void editItems(Item item) {
        JDialog editWindow = new JDialog();
        editWindow.setDefaultCloseOperation(HIDE_ON_CLOSE);
        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        editWindow.add(form);

        JLabel time = new JLabel();
        JLabel error = new JLabel();
        error.setForeground(Color.red);
        time.setForeground(Color.red);

        JTextField Time = new JTextField(item.getItemStartTime().format(DateTimeFormatter.ofPattern("hh:mm")), 15);
        JTextField ItemTitle = new JTextField(item.getItemtitle(), 15);

        form.add(error);
        form.add(time);

        form.add(new JLabel("Item Title"));
        form.add(ItemTitle);
        form.add(new JLabel("Event Time HH:FF Fornat"));
        form.add(Time);

        ActionListener window;
        window = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getActionCommand().equals("Save")) {
                    // DataManager.getEventManager().addEventToManager(new Event(EventTitle.getText(), Time.getText(), Date.getText(), Location.getText()));

                    try {

                        item.setItemtitle(ItemTitle.getText());
                        item.setItemStartTime(Time.getText());
                        GuiViewer.updateView();
                        editWindow.dispose();
                        //IMPLEMENT EVENT UPDATE window
                        for (Event event : DataManager.getEventManager().getEventList()) {
                            if (event.getAgendaItem().contains(item)) {
                                System.out.println("Foun the item through loop");
                                //viewEventWindow.dispose(); //-- needed if not disposed in the following method
                                GuiViewer.viewEvent(event);
                            }
                        }

                    } catch (DateTimeParseException exeption) {
                        System.out.println("Date and time format is wrong");
                        time.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss")));
                        error.setText("   DateTimeFormat error");
                    } catch (Exception allexeption) {
                        System.out.println("Something went wrong");
                        error.setText("Something went wrong");
                    }

                } else if (e.getActionCommand().equals("Cancel")) {
                    editWindow.dispose();
                }
            }
        };

        JButton cancel = new JButton("Cancel");
        cancel.addActionListener(window);
        form.add(cancel);
        JButton save = new JButton("Save");//Added button add
        save.addActionListener(window);
        form.add(save);

        editWindow.setSize(400, 400);
        editWindow.setVisible(true);
    }

    public void viewMenuBar() { //Add menubar to viewer with file (Load and save) and exit

        GUIControl controls = new GUIControl();
        menubar = GUIControl.getMb();
        this.setJMenuBar(menubar);
        menubar.add(GUIControl.getFile());
        menubar.add(GUIControl.getExit());
        GUIControl.getFile().add(GUIControl.getLoadFile());
        GUIControl.getFile().add(GUIControl.getSaveFile());

    }

}
