# certificateKey

example command line arguments already checked on dev servers
/mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/public/cert/certificate --> -c
/mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/private/private.txt --> -k
MD5-h
SHA-256-h

/mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/log/logFile.txt --> -l
/mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/files --> -p
/mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/reg/regFile.txt --> -r

ichecker createCert -k /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/private/private.txt -c /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/public/cert/certificate

ichecker createReg -r /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/reg/regFile.txt -p /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/files -l /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/log/logFile.txt -h MD5 -k /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/private/private.txt

ichecker check -r /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/reg/regFile.txt -p /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/files -l /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/log/logFile.txt -h MD5 -c /mnt/disk1/ogrenci/ogr/b21627868/4.sınıf/try/public/cert/certificate