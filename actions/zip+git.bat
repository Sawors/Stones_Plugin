cd "C:\Users\sosol\AppData\Roaming\.minecraft\mods et profils\1.18 optifine\resourcepacks\STONES_1.18.1"
rmdir ResourcePack /s /q
mkdir ResourcePack
cd ResourcePack
mkdir assets
cd ..
xcopy assets "ResourcePack/assets" /E
xcopy pack.mcmeta ResourcePack
xcopy pack.png ResourcePack
cd ResourcePack
"C:\Program Files\7-Zip\7z" a rp.zip *
move rp.zip ..
cd ..
rmdir ResourcePack /s /q
git add *
git commit -m "Automatic commit made by the RP ziper %DATE% / %TIME%"
git push origin main

