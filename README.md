# Simple_Search_Engine
A simple search engine that processes a collection of data and searches for a word or phrase. It reads text lines from a file and processes complex queries containing word sequences and uses several strategies that determine how to match data.

The program can use different strategies for search, displaying results for lines:
- containing all words from the query
- containing at least one word from the query
- does not contain any word from the query.

The results do not contain any duplicates.

It uses an inverted index that maps each word to all positions/lines/documents in which the word occurs. As a result, when we receive a query, we can immediately find the answer without any comparisons.

The file used to test the search engine is included in the repository (text.txt)
