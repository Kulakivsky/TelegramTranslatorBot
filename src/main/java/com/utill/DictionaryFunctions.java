package com.utill;

import com.TgDictionaryBot;
import com.dataBase.DictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.utill.messages.DictionaryCommands.ENGLISH_DICTIONARY;
import static com.utill.messages.DictionaryCommands.FRENCH_DICTIONARY;
import static com.utill.messages.DictionaryMessages.*;

@Component
public class DictionaryFunctions {

    @Autowired
    private CheckArrayOfEnteredWords checkArrayOfEnteredWords;
    @Autowired
    private TgDictionaryBot tgDictionaryBot;
    @Autowired
    private DictionaryDao dictionaryDao;

    public void updateWord(String[] enteredText, String primaryDictionary, String translationDictionary) {
        if (checkArrayOfEnteredWords.checkArray(enteredText, 2)) {
            String wordToUpdate = enteredText[0];
            String newTranslation = enteredText[1];

            dictionaryDao.updateTranslation(newTranslation, wordToUpdate, primaryDictionary, translationDictionary);
            tgDictionaryBot.sendMessage(String.format(WORD_UPDATED_SUCCESSFULLY, wordToUpdate, newTranslation));
            tgDictionaryBot.sendMessage("Enter another word to UPDATE it's translation");

        } else tgDictionaryBot.sendMessage(UPDATE_A_WORD_COMMAND_ENTERED_INCORRECTLY);
    }

    public void deleteWord(String[] enteredText, String primaryDictionary) {
        String wordToDelete = enteredText[0];

        if (checkArrayOfEnteredWords.checkArray(enteredText, 1)) {
            dictionaryDao.deleteWord(wordToDelete, primaryDictionary);
            tgDictionaryBot.sendMessage(String.format(WORD_DELETED_SUCCESSFULLY, wordToDelete));
            tgDictionaryBot.sendMessage("Enter another word to DELETE from the dictionary");

        } else tgDictionaryBot.sendMessage(DELETE_A_WORD_COMMAND_ENTERED_INCORRECTLY);
    }

    public void addWord(String[] enteredText, String primaryDictionary) {
        String baseWord = enteredText[0];
        String translationWord = enteredText[1];

        if (checkArrayOfEnteredWords.checkArray(enteredText, 2)) {
            dictionaryDao.saveNewWord(baseWord, translationWord, primaryDictionary);

            tgDictionaryBot.sendMessage(String.format(NEW_WORD_SUCCESSFULLY_ADDED, translationWord));
            tgDictionaryBot.sendMessage("Enter another word to ADD to the dictionary");
        } else tgDictionaryBot.sendMessage(ADD_WORD_COMMAND_ENTERED_INCORRECTLY);
    }

    public String translate(String enteredText, String languageToTranslateTo, String dictionaryType){
        return dictionaryDao.getTranslation(enteredText, languageToTranslateTo, dictionaryType);
    }

    public boolean changeTranslation(boolean isEnglish) {
        isEnglish = !isEnglish;
        if (isEnglish) {
            tgDictionaryBot.sendMessage(String.format(CURRENT_LANGUAGE_PAIR, ENGLISH_DICTIONARY, FRENCH_DICTIONARY));
        } else {
            tgDictionaryBot.sendMessage(String.format(CURRENT_LANGUAGE_PAIR, FRENCH_DICTIONARY, ENGLISH_DICTIONARY));
        }
        return isEnglish;
    }
}
