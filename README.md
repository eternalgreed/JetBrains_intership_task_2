# JetBrains_intership_task_2
# Задание 
Задание на Java (можно и на Kotlin, если это удобнее, но используя те же интерфейсы – это облегчит мне проверку).
Итак, твоя задача -  реализовать следующий интерфейс:

 

public interface Runner<T> {

 

    /**

     * Runs a set of interdependent processors many times until null is produced by any of them

     * @param maxThreads - maximum number of threads to be used

     * @param maxIterations - maximum number of iterations to run

     * @param processors - a set of processors

     * @return a map, where the key is a processor id, and the value is a list of its outputs in the order of iterations

     * @throws ProcessorException if a processor throws an exception, loops detected, or some input ids not found

     */

    Map<String, List<T>> runProcessors(Set<Processor<T>> processors, int maxThreads, int maxIterations) throws ProcessorException;

 

}

 

где интерфейс Processor определен следующим образом и может быть реализован в тестах:

 

public interface Processor<T> {

 

    /**

     * @return processor id, immutable, unique among all instances, not null

     */

    String getId();

 

    /**

     * @return a list of processors that have to be executed before this one

     *         and whose results must be passed to Processor::process,

     *         immutable, can be null or empty, both means no inputs

     */

    List<String> getInputIds();

 

    /**

     * @param input outputs of the processors whose ids are returned by Processor::getInputIds, not null, but can be empty

     * @return output of the processing, null if no output is produced

     * @throws ProcessorException if error occurs during processing

     */

    T process(List<T> input) throws ProcessorException;

 

}

 

а ProcessorException extends Exception

 

Реализация должна удовлетворять следующим требованиям:

- задача метода runProcessors – запускать весь набор «процессоров» в несколько итераций (не более maxIterations) и возвратить список результатов каждого для всех полных итераций;

- в рамках каждой итерации ни один «процессор» не запускается пока не будут запущены все те, что соответствуют его input ids;

- некоторые «процессоры» возвращают пустые списки input ids, они являются источником данных и могут запускаться сразу;

- «процессоры» могут (и должны) выполняться в несколько потоков, но не более чем maxThreads;

- в каждой итерации каждый «процессор» запускается ровно один раз;

- может запускаться больше одной итерации одновременно, но ни один «процессор» не может запускаться параллельно самому себе;

- последовательность итераций для каждого «процессора» должна соблюдаться, то есть он не может быть запущен в итерации, если еще не завершился в предыдущей;
- если хоть один «процессор» кидает исключение, все остальные потоки должны прерываться и runProcessors тоже должен кидать исключение;
- также runProcessors должен кидать исключение, если граф зависимостей содержит циклы или неизвестные input ids

- если хоть один «процессор» возвращает null, результаты этой и всех последующих (если они уже запущены) итераций должны игнорироваться и runProcessors должен возвращать результат всех предыдущих итераций;


Очень бы хотелось, чтобы при решении использовались лишь стандартные библиотеки Java (или Kotlin).
