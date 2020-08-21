package com.example.application.views.testtask;

import com.example.application.data.service.StringParser;
import com.example.application.data.service.Task;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.internal.MessageDigestUtil;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.apache.commons.io.IOUtils;
import org.vaadin.olli.FileDownloadWrapper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;


@Route(value = "test", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
@PageTitle("Test task")
@CssImport("styles/views/test-task/test-task.css")
public class TestTaskView extends Div {

    private TextField inputText = new TextField();
    private TextField resultText = new TextField();
    private ComboBox<String> taskBox = new ComboBox<>();
    private Component component;

    private Button calculate = new Button("Calculate");
    private Button save = new Button("Save");

    private MemoryBuffer buffer = new MemoryBuffer();
    private Upload upload = new Upload(buffer);
    private FileDownloadWrapper link;

    public TestTaskView() {
        setId("test-task-view");
        VerticalLayout wrapper = createWrapper();
        createTitle(wrapper);
        createFormLayout(wrapper);
        createBoxLayout(wrapper);

        link = new FileDownloadWrapper("data.txt", () ->
                (inputText.getValue() + "\n" + taskBox.getValue()).getBytes());
        link.wrapComponent(save);

        createButtonLayout(wrapper);

        inputText.setPlaceholder("Input substring ; string");
        calculate.addClickListener(e -> {
            if (taskBox.getValue().equals("Finding words in lexicographical order")) {
                if (!inputText.getValue().contains(";")) {
                    resultText.setValue("Enter words in format: firstString ; secondString!");
                } else {
                    List<String[]> result = StringParser.stringParser(inputText.getValue());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(Arrays.toString(Task.subSortedArray(result.get(0), result.get(1))));
                    resultText.setValue(stringBuilder.toString());
                }
            } else {
                try {
                    resultText.setValue(Task.expanded(Long.parseLong(inputText.getValue().trim())));
                } catch (java.lang.NumberFormatException exception) {
                    resultText.setValue("Enter digit greater then 0!");
                }
            }
            Notification.show("Calculated");
        });
        save.addClickListener(e -> {
            Notification.show("Not implemented");
        });
        resultText.setReadOnly(true);
        taskBox.setItems("Finding words in lexicographical order", "Digit extended form");
        taskBox.setValue("Finding words in lexicographical order");
        taskBox.addValueChangeListener(e -> {
            if (e.getValue().equals("Finding words in lexicographical order")) {
                inputText.setValue(" ; ");
                inputText.setPlaceholder("Input substring ; string");
            } else if (e.getValue().equals("Digit extended form")){
                inputText.clear();
                inputText.setPlaceholder("Input digit");
            }
        });

        upload.setDropAllowed(false);
        upload.setAcceptedFileTypes(".txt");
        upload.addSucceededListener(event -> {
            component = createComponent(event.getMIMEType(),
                    event.getFileName(), buffer.getInputStream());
            String[] splitString = component.getElement().toString().split("\n");
            if (splitString[1].equals("Finding words in lexicographical order")) {
                taskBox.setValue(splitString[1]);
                inputText.setValue(splitString[0]);
            } else if (splitString[1].equals("Digit extended form")){
                taskBox.setValue(splitString[1]);
                inputText.setValue(splitString[0]);
            }
        });

        add(wrapper);
    }

    private void createTitle(VerticalLayout wrapper) {
        H1 h1 = new H1("Test task");
        wrapper.add(h1);
    }

    private VerticalLayout createWrapper() {
        VerticalLayout wrapper = new VerticalLayout();
        wrapper.setId("wrapper");
        wrapper.setSpacing(false);
        return wrapper;
    }

    private void createFormLayout(VerticalLayout wrapper) {
        FormLayout formLayout = new FormLayout();
        addFormItem(wrapper, formLayout, inputText, "Input Data");
        addFormItem(wrapper, formLayout, resultText, "Result");
    }

    private void createBoxLayout(VerticalLayout wrapper) {
        FormLayout formLayout = new FormLayout();
        addFormItem(wrapper, formLayout, taskBox, "Task");
    }

    private void createButtonLayout(VerticalLayout wrapper) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        buttonLayout.setWidthFull();
        buttonLayout
                .setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        buttonLayout.add(calculate);
        buttonLayout.add(link);
        buttonLayout.add(upload);
        wrapper.add(buttonLayout);
    }

    private FormLayout.FormItem addFormItem(VerticalLayout wrapper,
                                            FormLayout formLayout, Component field, String fieldName) {
        FormLayout.FormItem formItem = formLayout.addFormItem(field, fieldName);
        wrapper.add(formLayout);
        field.getElement().getClassList().add("full-width");
        return formItem;
    }

    private Component createComponent(String mimeType, String fileName,
                                      InputStream stream) {
        if (mimeType.startsWith("text")) {
            return createTextComponent(stream);
        }
        Div content = new Div();
        String text = String.format("Mime type: '%s'\nSHA-256 hash: '%s'",
                mimeType, MessageDigestUtil.sha256(stream.toString()));
        content.setText(text);
        return content;

    }

    private Component createTextComponent(InputStream stream) {
        String text;
        try {
            text = IOUtils.toString(stream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            text = "exception reading stream";
        }
        return new Text(text);
    }
}
