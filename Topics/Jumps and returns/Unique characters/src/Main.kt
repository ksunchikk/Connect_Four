fun main() {
    val word = readLine()!!
    var cnt = 0
    loop@for(i in 0..word.length -1){
        for(j in  0..word.length -1){
            if  (word[i] == word[j] && i != j) continue@loop
        }
        cnt++
    }
    print(cnt)
}
