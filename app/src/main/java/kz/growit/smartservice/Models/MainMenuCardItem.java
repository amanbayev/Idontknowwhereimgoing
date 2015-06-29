package kz.growit.smartservice.Models;

/**
 * Created by Талгат on 28.06.2015.
 */
public class MainMenuCardItem {
    private String introText;
    private String buttonText;
    private int tag;

    public MainMenuCardItem(String introText, String buttonText, int tag) {
        this.introText = introText;
        this.buttonText = buttonText;
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public MainMenuCardItem() {
    }

    public String getIntroText() {
        return introText;
    }

    public void setIntroText(String introText) {
        this.introText = introText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }
}
