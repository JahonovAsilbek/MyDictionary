package uz.revolution.mydictionary.models

import uz.revolution.mydictionary.database.entities.Category
import uz.revolution.mydictionary.database.entities.Word

class CategoryData(var category: Category, var wordList: ArrayList<Word>)