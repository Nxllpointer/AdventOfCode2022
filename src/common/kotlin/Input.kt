fun <ProcessedInputType> getAOCInput(inputProcessor: (rawInput: String) -> ProcessedInputType): ProcessedInputType {
    val input = ClassLoader.getSystemResource("input.txt").readText()
    return inputProcessor(input)
}
