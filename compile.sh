# TODO: make more general. Compile all .dot to .svg and .png...
dot -T svg docs/classes.dot > docs/classes.svg
dot -T svg docs/measure_example.dot > docs/measure_example.svg
dot -T png docs/classes.dot > docs/classes.png
dot -T png docs/measure_example.dot > docs/measure_example.png

# TODO: compile all .tex to .pdf
cd docs
pdflatex design-milestone.tex
pdflatex team-contract.tex
cd ..
