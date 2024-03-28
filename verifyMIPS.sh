#!/bin/bash

# On vide le dossier mips
rm -rf mips
mkdir mips
mkdir mips/out

# Vérifier si le nombre d'arguments est différent de 1
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <dossier>"
    exit 1
fi

# Vérifier si le dossier existe
if [ ! -d "$1" ]; then
    echo "Le dossier $1 n'existe pas."
    exit 1
fi

# Afficher le contenu du dossier
#echo "Contenu du dossier $1 :"
#ls -al "$1"

errorCount=0

# Pour chaque fichier faire
for file in "$1"/*; do
    # Vérifier si le fichier est un fichier
    if [ -f "$file" ]; then
        # Vérifier si le fichier est un fichier PLIC
        if file "$file" | grep -q "plic"; then
            java -jar plic.jar "$file" >> mips/"$(basename "$file" .plic)".mips
            java -jar MARS.jar mips/"$(basename "$file" .plic)".mips >> mips/out/"$(basename "$file" .plic)".txt
            # Si le .txt généré est différent de l'approved on affiche un message
            if ! diff mips/out/"$(basename "$file" .plic)".txt approved/"$(basename "$file" .plic)".txt; then
                echo "Fichier $(basename "$file" .plic).txt différent."
                errorCount=$((errorCount+1))
            fi
        fi
    fi
done

echo "Vérification terminée avec" $errorCount "erreurs."
