package connectfour
class Player(name: String){
    var name: String = name
    var score: Int = 0
}
class ConnectFour{
    val DIMENSION_PATTERN = Regex("([0-9]+)\\s*(x|X)\\s*([0-9]+)")
    val TURN_PATTERN = Regex("[0-9]+")
    val FULL_FIELD_PATTERN = Regex("((o|\\*)\\s?)*")
    val WON_PATTERN_1 = Regex(".*o o o o.*")
    val WON_PATTERN_2 = Regex(".*\\* \\* \\* \\*.*")
    var rows: Int = 6
    var columns: Int = 7
    var firstPlayer: Player
    var secondPlayer: Player
    var fieldList: MutableList<MutableList<String>>
    var amountOfGames = 1
    var fullField = false
    var turn = true
    init{
        println("Connect Four")
        print("First player's name:\n")
        this.firstPlayer = Player(readLine()!!)
        print("Second player's name:\n")
        this.secondPlayer = Player(readLine()!!)
        this.dimensions()
        fieldList = MutableList(columns){ MutableList(rows){" "} }
        this.amountOfGames()
        this.start()

    }
    fun start(){
        println("${this.firstPlayer.name} VS ${this.secondPlayer.name}\n${rows} X ${columns} board")
        if(amountOfGames == 1) println("Single game")
        else println("Total ${this.amountOfGames} games")
    }
    fun dimensions(){
        while(true) {
            print("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)\n")
            val dem = readLine()!!.trim()
            when {
                (dem.isEmpty()) -> break
                !this.DIMENSION_PATTERN.matches(dem) -> {
                        print("Invalid input\n")
                        continue
                    }
                else -> {
                        if (dem[0].digitToInt() in 5..9) {
                            if (dem[dem.length - 1].digitToInt() in 5..9) {
                                this.rows = dem[0].digitToInt()
                                this.columns = dem[dem.length - 1].digitToInt()
                                break
                            } else println("Board columns should be from 5 to 9")
                        } else println("Board rows should be from 5 to 9")
                    }
            }
        }
    }
    fun amountOfGames(){
        while(true){
            println("Do you want to play single or multiple games?\n" +
                    "For a single game, input 1 or press Enter\n" +
                    "Input a number of games:")
            val amount = readLine()!!
            when{
                amount.isEmpty() -> {
                    break
                }
                TURN_PATTERN.matches(amount) ->{
                    if (amount.toInt() == 1) break
                    else if (amount.toInt() == 0)println("Invalid input")
                    else {
                        amountOfGames = amount.toInt()
                        break
                    }
                }
                else -> println("Invalid input")
            }
        }
    }
    fun drawBoard() {
        for(i in 1..this.columns) print(" $i")
        print("\n")
        for (i in rows-1 downTo 0){
            repeat(this.columns){print("|${fieldList[it][i]}")}
            print("|\n")
        }
        repeat(this.columns){print("==")}
        print("=\n")
    }
    fun everyTurn(turn: Boolean, tmp: Int):Boolean {
        var j = 0
        while(j <= fieldList[tmp].size - 1){
            if (fieldList[tmp][j] == " ") {
                if (turn) fieldList[tmp][j] = "o"
                else fieldList[tmp][j] = "*"
                drawBoard()
                return !turn
            }
            j++
        }
        return turn
    }
    fun processOfGame(): Boolean {

        do{
            var k = turn
            this.fullField = false
            if(turn) print("${firstPlayer.name}\'s turn:\n")
            else print("${secondPlayer.name}\'s turn:\n")
            val playerChoice = readLine()!!
            if(playerChoice != "end"){
                if(TURN_PATTERN.matches(playerChoice)){
                    val tmp = playerChoice.toInt() - 1
                    if(tmp+1 !in 1..columns){
                        print("The column number is out of range (1 - ${this.columns})\n")
                        continue
                    }
                    if(FULL_FIELD_PATTERN.matches(fieldList[tmp].joinToString(" "))){
                        println("Column ${tmp+1} is full")
                        continue
                    }
                    turn = everyTurn(turn, tmp)
                    if(checkForWin()){
                        if(!turn) {
                            println("Player ${firstPlayer.name} won")
                            firstPlayer.score+=2
                        }
                        else {
                            println("Player ${secondPlayer.name} won")
                            secondPlayer.score+=2
                        }
                        return true
                    }
                    var full = false
                    for(m in 0..fieldList.size-1) {
                        val result = fieldList[m].joinToString(" ")
                        if (FULL_FIELD_PATTERN.matches(result)) {
                            full = true
                            continue
                        }
                        full = false
                        break
                    }
                    if(full) {
                        println("It is a draw")
                        fullField = true
                        firstPlayer.score++
                        secondPlayer.score++
                        return true
                    }
                }
                else print("Incorrect column number\n")
            }
        } while(playerChoice != "end")
        return false
    }
    fun checkForWin(): Boolean {
        for (i in 0..fieldList.size - 1) {
            val result = fieldList[i].joinToString(" ")
            if (result.contains(WON_PATTERN_1) || result.contains(WON_PATTERN_2)) return true
        }
        for (k in 0..fieldList[0].size - 1) {
            val result = mutableListOf<String>()
                .apply { repeat(fieldList.size) { add(fieldList[it][k]) } }
                .joinToString(" ")
            if (WON_PATTERN_1.matches(result) || WON_PATTERN_2.matches(result)) return true
        }
        for (i in fieldList.size - 1 downTo 0) {
            var k = i
            val tmp_list = mutableListOf<String>()
            for (j in 0..fieldList.size - i - 1) {
                tmp_list.add(fieldList[k][j])
                k++
            }
            if (tmp_list.size >= 4) {
                val result = tmp_list.joinToString(" ")
                if (WON_PATTERN_1.matches(result) || WON_PATTERN_2.matches(result)) return true
            }
            tmp_list.clear()
        }
        for (i in 0..fieldList.size - 1) {
            var k = i
            val tmp_list = mutableListOf<String>()
            for (j in fieldList.size - i - 1 downTo 0) {
                tmp_list.add(fieldList[k][j])
                k++
            }
            if (tmp_list.size >= 4) {
                val result = tmp_list.joinToString(" ")
                if (WON_PATTERN_1.matches(result) || WON_PATTERN_2.matches(result)) return true
            }
            tmp_list.clear()
        }
        return false
    }
    fun multipleProcessOfGame(){
        var game = amountOfGames
        for(i in 1..game){
            if(amountOfGames != 1) println("Game #$i")
            this.drawBoard()
            if(processOfGame()) {
                println("Score")
                println("${firstPlayer.name}: ${firstPlayer.score} ${secondPlayer.name}: ${secondPlayer.score}")
                fieldList = MutableList(columns) { MutableList(rows) { " " } }
            }
            else break
        }
            print("Game over!")
    }
}

fun main() {
    val game = ConnectFour()
    game.multipleProcessOfGame()
}