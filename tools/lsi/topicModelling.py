# -*- coding: utf-8 -*-

from gensim import corpora, models, similarities
import sys
def topicModelling(text): 
	maxTopic = -999999
	maxIndex = -1
	for pair in lsi[tfidf[dictionary.doc2bow(text.split())]]:
		if maxTopic < pair[1]:
			maxTopic = pair[1]
			maxIndex = pair[0]
	return maxIndex

def extractStrings(topics):
	listTopics = list();
	for pair in topics:
		wordList = u''
		terms = pair[1].split('+')
		for term in terms:
			p = term.split('*') 
			if len(p) == 2:
				wordList = wordList + p[1]
		listTopics.append(wordList)
	print len(listTopics)
	return listTopics

if __name__ == '__main__':
	fileName = sys.argv[1]
	f = open(fileName,'r')
	lang = ''
	text = ''
	for line in f : 
		args = line.split('##')
		lang = args[1]
		print lang
		text = args[0]
		#print text	
	lsi = models.LsiModel.load('E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\twitter\\models\\'+lang+'.lsi')
	dictionary = corpora.Dictionary.load('E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\twitter\\models\\'+lang+'.dict')
	tfidf = models.TfidfModel.load('E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\twitter\\models\\'+lang+'.tfidf')
	topics = lsi.print_topics(15)
	topicStrings  = extractStrings(topics)
	maxIndex = topicModelling(text)
	#print maxIndex
	f = open('E:\\test', 'w')
	f.write(topicStrings[maxIndex].encode('utf8'))
	f.close()
	print 'complete'