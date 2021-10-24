fun main() {
    val word = readLine()!!
    for(c in word){
        if(c.isDigit()){
            print(c)
            break
        }
    }
}
