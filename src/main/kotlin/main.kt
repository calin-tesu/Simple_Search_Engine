package search

import java.io.File

//Stores a list with the words(stream of chars delimited by " ") that appear in every line of the input file
val listOfWords = mutableListOf<MutableList<String>>()

fun main(args: Array<String>) {
    //val file = "C:\\text.txt"
    val file = args[1]
    val content = File(file)
    content.forEachLine { line ->
        listOfWords.add(line.split(" ") as MutableList<String>)
    }

/*
Key-Value pair that stores the indices where every word from the file can be found
ex. "erick" to [3, 6 ,14] */
    val invertedDataMap = newInvertedData()

    do {
        println(
            """                
===== Menu =====
1. Find a person
2. Print all people
0. Exit
"""
        )

        //If the input is null or not an Int we choose 69 just for convenience (can choose any other integer)
        when (readLine()?.toIntOrNull() ?: 69) {
            1 -> printReport(searchContent(invertedDataMap))
            2 -> printReport(listOfWords.indices.toMutableSet())
            0 -> break
            else -> println("Incorrect option! Try again.")
        }
    } while (true)

    println("Bye!")
}

/*
The keys of the map stores the names of people (or any word, aka string delimited by " ") that appear in the file
The values stores an array of indices of the lines where that words can be founded in the file */
fun newInvertedData(): MutableMap<String, MutableList<Int>> {
    val map: MutableMap<String, MutableList<Int>> = mutableMapOf()
    for (i in listOfWords.indices) {
        for (item in listOfWords[i]) {
            //The created Map have the keys stored in lowercase so will be easy to do a case insensitive search
            if (map.containsKey(item.lowercase())) {
                map[item.lowercase()]!!.add(i)
            } else {
                map[item.lowercase()] = mutableListOf(i)
            }
        }
    }

    return map
}

fun searchContent(invertedDataMap: MutableMap<String, MutableList<Int>>): MutableSet<Int> {
    var searchType: String
    do {
        println("Select a matching strategy: ALL, ANY, NONE")
        searchType = readLine()!!.uppercase()
        if (searchType in listOf("ALL", "ANY", "NONE")) break
    } while (true)

    println("Enter a name or email to search all suitable people.")
    //List of term search
    val termsOfSearch = readLine()!!.lowercase().split(" ")

    //Stores the indices at which we can find the results of search
    var searchResults = mutableSetOf<Int>()

    when (searchType) {
        "ANY" ->
            for (word in termsOfSearch) {
                if (invertedDataMap.containsKey(word)) searchResults.addAll(invertedDataMap[word]!!.toList())
            }

        "ALL" -> {
            //Intersection of all term search
            if (!invertedDataMap.containsKey(termsOfSearch.first())) return searchResults
            searchResults.addAll(invertedDataMap[termsOfSearch.first()]!!.toList())
            for (word in termsOfSearch) {
                if (!invertedDataMap.containsKey(word)) {
                    searchResults.clear()
                    return searchResults
                } else {
                    searchResults = (searchResults intersect invertedDataMap[word]!!) as MutableSet<Int>
                }
            }
        }

        "NONE" -> {
            //Substitution of all term search
            searchResults.addAll(listOfWords.indices)
            for (word in termsOfSearch) {
                if (invertedDataMap.containsKey(word))
                    searchResults = (searchResults subtract invertedDataMap[word]!!) as MutableSet<Int>
            }
        }
    }

    return searchResults
}

fun printReport(searchResults: MutableSet<Int>) {
    if (searchResults.isNotEmpty()) {
        println("${searchResults.size} persons found")
        for (i in searchResults) {
            println(listOfWords[i].joinToString(" "))
        }
    } else {
        println("No matching people found.")
    }
}