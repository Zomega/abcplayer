# TODO: make more general. Compile all .dot to .svg and .png...
dot -T svg design/classes.dot > design/classes.svg
dot -T svg design/measure_example.dot > design/measure_example.svg
dot -T png design/classes.dot > design/classes.png
dot -T png design/measure_example.dot > design/measure_example.png

# TODO: compile all .tex to .pdf
cd design
pdflatex design_notes.tex
cd ../docs
pdflatex team-contract.tex
cd ..
