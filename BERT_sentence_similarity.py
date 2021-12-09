
from bert_serving.client import BertClient
from sklearn.metrics.pairwise import cosine_similarity

# Uncomment the following line if the BERT server that is running locally (on the same physical server that the client will be running on).
#client = BertClient()

# Uncomment the following line if the BERT server is running remotely (on a different physical server than the client will be running on).
# You must specify the IP of the remote server and the ports
client = BertClient(ip='10.3.50.61', port=5555, port_out=5556)


first_sentence = 1
second_sentence = 2

# Encode the sentences using the BERT client.
sentences = client.encode([sentence0, sentence1, sentence2], show_tokens=True, is_tokenized=True)


cos_sim = cosine_similarity(sentences[0][first_sentence][:].reshape(1,-1),sentences[0][ second_sentence][:].reshape(1,-1))

# Show the sentences and their cosine similarity, but leave off the [CLS] at the beginning and [SEP] at the end
if first_sentence == 0 and second_sentence == 1:
    		print("\n\nThe cosine similarity between sentence:\n" + str(' '.join(sentence0)) + "\n\nand sentence:\n\n" + str(' '.join(sentence1)) + "\nis " + str(cos_sim[0][0]))
if first_sentence == 0 and second_sentence == 2:
    		print("\n\nThe cosine similarity between sentence:\n" + str(' '.join(sentence0)) + "\n\nand sentence:\n\n" + str(' '.join(sentence2)) + "\nis " + str(cos_sim[0][0]))
if first_sentence == 1 and second_sentence == 2:
    		print("\n\nThe cosine similarity between sentence:\n" + str(' '.join(sentence1)) + "\n\nand sentence:\n\n" + str(' '.join(sentence2)) + "\nis " + str(cos_sim[0][0]))

# Show the encoded versions of the sentences for comparison
print("\n******\nThe encoded sentences are")
print(*sentences[1][first_sentence][1:len(sentences[1][first_sentence][:])-1])
print("\nand\n")
print(*sentences[1][second_sentence][1:len(sentences[1][second_sentence][:])-1])
print("\n")
