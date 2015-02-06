PGFS=dfd-context.pgf dfd-0.pgf dfd-3.pgf

sds.pdf : sds.tex $(PGFS)
	pdflatex --shell-escape $<
	pdflatex --shell-escape $<

%.pgf : %.dia
	dia -e $@ -t pgf-tex $<

clean :
	-rm -f *.pdf *.pgf *.log *.aux *.out *.toc *.synctex.gz *.dvi
