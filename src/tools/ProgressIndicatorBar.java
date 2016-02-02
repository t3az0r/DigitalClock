package tools;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ProgressIndicatorBar extends StackPane {
  final private ReadOnlyDoubleProperty workDone;
  final private double totalWork;

  final private ProgressBar bar  = new ProgressBar();
  final private Text        text = new Text();
  final private String      labelFormatSpecifier;
  private String            styleClassSpecifier;

    public String getStyleClassSpecifier() {
        return styleClassSpecifier;
    }

    public void setStyleClassSpecifier(String styleClassSpecifier) {
        this.styleClassSpecifier = styleClassSpecifier;
    }

  final private static int DEFAULT_LABEL_PADDING = 10;

  public ProgressIndicatorBar(final ReadOnlyDoubleProperty workDone, final double totalWork, final String labelFormatSpecifier) {
    this.workDone  = workDone;
    this.totalWork = totalWork;
    this.labelFormatSpecifier = labelFormatSpecifier;

    this.text.setFont(new Font("Helvetica", 24.0));
    syncProgress();
    workDone.addListener(new ChangeListener<Number>() {
      @Override public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
        syncProgress();
      }
    });
    //bar.getStyleClass().add("orangered-bar");
    bar.setMaxWidth(Double.MAX_VALUE); // allows the progress bar to expand to fill available horizontal space.
    getChildren().setAll(bar, text);
  }

  // synchronizes the progress indicated with the work done.
  private void syncProgress() {
    //bar.getStyleClass().add(styleClassSpecifier);
    if (workDone == null || totalWork == 0) {
      text.setText("");
      bar.setProgress(ProgressBar.INDETERMINATE_PROGRESS);
    } else {
      text.setText(String.format(labelFormatSpecifier, workDone.get())); // Math.ceil(workDone.get())));
      bar.setProgress(workDone.get() / totalWork);
    }

    bar.setMinHeight(text.getBoundsInLocal().getHeight() + DEFAULT_LABEL_PADDING * 2);
    bar.setMinWidth (text.getBoundsInLocal().getWidth()  + DEFAULT_LABEL_PADDING * 2);

  }
}
