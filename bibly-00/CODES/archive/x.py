import xml.etree.ElementTree as ET
tree = ET.parse('kjv.xml')
root = tree.getroot()

for bn in root.findall('BIBLEBOOK'):

	bname=bn.attrib['bname']

	for cn in bn.findall('CHAPTER'):

		cnumber=cn.attrib['cnumber']

		for vn in cn.findall('VERS'):

			vnumber=vn.attrib['vnumber']
			vers=vn.text

			print(bname+" "+cnumber+":"+vnumber+" "+vers)


