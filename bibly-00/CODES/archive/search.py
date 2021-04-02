text = input('search: ')
file = open("kjv.vpl")
for vpl in file:
	if text in vpl:
		 print(vpl)

