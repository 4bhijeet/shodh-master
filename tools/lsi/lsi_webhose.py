# -*- coding: utf-8 -*-

from nltk.corpus import stopwords
import json
from gensim import corpora, models, similarities
import re
import string 
from sets import Set
def create_dict(fileName, lang):
	map = {}
	map['en'] = 'english'
	map['ru'] = 'russian'
	map['de'] = 'german'
	map['fr'] = 'french'
	with open(fileName) as data_file:
		data = json.load(data_file)
		print len(data)
		field = 'text_'+lang
		_digits = re.compile('\d')
		_regex = re.compile('[%s]' % re.escape(string.punctuation))
		for post in data :
			if not _digits.search(post[field]):
				a = _regex.sub('',post[field].lower())
		dictionary = corpora.Dictionary(post[field].lower().split() for post in data if not _digits.search(post[field]))
		print dictionary
		stoplist = ['RT', 'rt', '-','—', '!', '?', ',','\"','\'']
		if lang != 'ar':
			stoplist = stoplist + stopwords.words(map[lang])
		stop_ids = [dictionary.token2id[stopword] for stopword in stoplist if stopword in dictionary.token2id]
		print len(stoplist)
        remove_ids = [item[0] for item in dictionary.items() if item[1].find('http') != -1 or item[1].find('@') != -1]
        dictionary.filter_tokens(remove_ids + stop_ids)
        dictionary.compactify()
        print dictionary
        return dictionary

class MyCorpus(object):
	def __init__(self, fileName, lang):
		self.fileName = fileName
		self.lang = lang
	def __iter__(self):
		with open(self.fileName) as data_file:
			data = json.load(data_file)
			for post in data:
				#print post['id']
				field = 'text_'+self.lang
				yield dictionary.doc2bow(post[field].lower().split())  

if __name__ == '__main__':
	langs = ['en', 'ar', 'fr', 'de', 'ru']
	
	for lang in langs:
		print 'Processing ' + lang + '...'
		corpus = MyCorpus('E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\webhose\\' + lang +'.json', lang)
		dictionary = create_dict('E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\webhose\\' + lang +'.json', lang)
		tfidf = models.TfidfModel(corpus)
		corpus_tfidf = tfidf[corpus]
		lsi = models.LsiModel(corpus_tfidf, id2word=dictionary, num_topics=15)
		topcs = lsi.print_topics(15)
		for tp in topcs:
			print tp
		lsi.save('E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\webhose\\models\\'+lang+'.lsi')
		dictionary.save('E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\webhose\\models\\'+lang+'.dict')
		tfidf.save('E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\webhose\\models\\'+lang+'.tfidf')
		print 'Models saved in E:\\MS Sem 1\\IR\\ProjectPartC\\FilteredTweets\\language_combine\\webhose\\models\\'
	#topicModelling()