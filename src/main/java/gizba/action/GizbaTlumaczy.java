package main.java.gizba.action;

import com.intellij.codeInsight.CodeInsightActionHandler;
import com.intellij.codeInsight.actions.BaseCodeInsightAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.awt.RelativePoint;
import main.java.gizba.translator.Translator;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

public class GizbaTlumaczy extends BaseCodeInsightAction  {

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null){
            return;
        }

        FileEditorManager fileEditorManager = FileEditorManager.getInstance(project);
        Editor selectedTextEditor = fileEditorManager.getSelectedTextEditor();
        if (selectedTextEditor == null){
            return;
        }

        SelectionModel selectionModel = selectedTextEditor.getSelectionModel();
        String selectedText = selectionModel.getSelectedText();

        Translator translator = new Translator();
        String dkToEn = translator.translate(selectedText, "dk", "en");

        String toDisplay;
        if(StringUtils.isNotBlank(dkToEn)){
            String enToPl = translator.translate(dkToEn, "en", "pl");
            toDisplay = "<html> EN: " + dkToEn.toLowerCase() + "<br> PL: " + enToPl.toLowerCase() + "</html>";
        }else{
            toDisplay = "Ups, tego słowa nie znam.";
        }

        JBPopupFactory jbPopupFactory = JBPopupFactory.getInstance();
        RelativePoint relativePoint = jbPopupFactory.guessBestPopupLocation(selectedTextEditor);
        JBPopup message = jbPopupFactory.createMessage(toDisplay);
        message.show(relativePoint);
    }

    @Override
    protected CodeInsightActionHandler getHandler() {
        return null;
    }
}
