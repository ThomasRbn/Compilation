#!/bin/bash

# On vide le dossier mips
rm -rf mips
mkdir mips

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

# Pour chaque fichier faire
for file in "$1"/*; do
    # Vérifier si le fichier est un fichier
    if [ -f "$file" ]; then
      #Si le fichier est déjà approuvé on passe
      if [ -f "approved/$(basename "$file" .plic).txt" ]; then
        echo "$(basename "$file" .plic) déjà approuvé."
        continue
      fi
        # Vérifier si le fichier est un fichier PLIC
        if file "$file" | grep -q "plic"; then
            java -jar plic.jar "$file" >> mips/"$(basename "$file" .plic)".mips
            java -jar MARS.jar mips/"$(basename "$file" .plic)".mips >> approved/"$(basename "$file" .plic)".txt
            echo "$(basename "$file" .plic) généré."
        fi
    fi
done
