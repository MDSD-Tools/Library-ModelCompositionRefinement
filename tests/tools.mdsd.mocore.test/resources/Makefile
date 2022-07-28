evaluation-init:
	mkdir -p ./evaluation/case-studies
	git clone git@github.com:PalladioSimulator/Palladio-Example-Models.git ./evaluation/case-studies/palladio-example-models
	git clone git@github.com:cocome-community-case-study/models.git ./evaluation/case-studies/cocome
	svn checkout --username anonymous --password anonymous https://svnserver.informatik.kit.edu/i43/svn/code/CaseStudies/MediaStore3/trunk/Model/MediaStore3_Model/ ./evaluation/case-studies/media-store-3
evaluation-clean:
	rm -r -f ./evaluation/
