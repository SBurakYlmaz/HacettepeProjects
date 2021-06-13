n = int(input("Sayı giriniz:"))
i = 1
copy = n
k = 2*n
def recursion(sayi):
    global i
    global k, copy
    if i < 2*sayi:
        diamond = i*"*"
        print(((copy-1)*" ")+diamond+((copy-1)*" "))
        i += 2
        copy -= 1
        return recursion(sayi)
    if k != 0 and i > 2*sayi:
        copy +=1
        diamond = (k-3)*"*"
        k -= 2
        print((copy*" ")+diamond+(copy*" "))
        return recursion(sayi)


recursion(n)
