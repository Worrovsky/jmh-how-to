## Подключение

1. Добавить зависимость


    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.35")
    testImplementation("org.openjdk.jmh:jmh-generator-annprocess:1.35")

2. Добавить плагин


    plugins {
        id("me.champeau.jmh") version "0.6.7"
    }

## Использование

Иерархия пакетов

    src
        \--jmh
            \--kotlin

Обязательно пакет НЕ по умолчанию

В нем класс с методом с `@Benchmark`


## Настройка тестов

### Виды настроек

@Fork

@Measurement

@Warmup

### Способы настройки

1. Через аннотации


    @Fork(1)
    @Warmup(iterations = 1)
    @Measurement(iterations = 3)
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    open class TestClass {
        @Benchmark
        ...
    }

2. Через плагин 


    // файл build.gradle.kts
    jmh {
        warmupIterations.set(1)
        iterations.set(2)
        fork.set(1)
    }

3. Запуск jar-ника и параметры командной строки

## Разное

[source #1](https://web.archive.org/web/20181018130828/http://java-performance.info:80/jmh)

### Состояния (State)

Задача: параметры или состояния для тестов

Варианты:

* Публичные классы с аннотацией `@State`: обычные или внутренние статические
  * конструкторы без параметров, если есть
* Сам класс-тест хранит состояние (помечен `@State`)

Как состояние соотносится с потоками:
* `Scope.Thread` - (по умолчанию) свое состояние на каждый поток
* `Scope.Benchmark` - одно состояние на весь тест. Для тестирования многопоточности
* `Scope.Group` - состояние на группу потоков (Group)

Примеры:

    public class JMHSample_03_States {
      @State(Scope.Benchmark)
      public static class BenchmarkState {
          volatile double x = Math.PI;
      }
    }

    @State(Scope.Thread)
    public class JMHSample_04_DefaultState {

      double x = Math.PI;

      @Benchmark
      public void measure() {
         x++;
      }
    }


### Fixtures (pre- и post- методы)

Аннотации **@Setup** и **@TearDown**

Когда выполняются методы - зависит от **Level**:
* **Level.Trial** - (по умолчанию) - один раз на бенчмарк (набор итераций)
* **Level.Iteration** - на каждую итерации
* **Level.Invocation** - на каждый вызов метода (аккуратно, влияет на общее время)

### "Мертвый" код

Опасно тестировать `void` методы. Всегда нужно возвращать результат.

Использовать заглушку `Blackhole`, если нельзя явно вернуть.

    @Benchmark
    fun testMethod1(bh: Blackhole) {
       bh.consume(log2(x))
    }
    
Константные значения передавать через состояние, иначе может закешировать

    @State(Scope.Thread)
    open class TestClass {
      private val x: Double = 0.43
        
      @Benchmark
      ...
    }

Избегать циклов - компилятор оптимизирует

Избегать тестов с fork = 0 (т. е. на хостовой ВМ): JVM опять же оптимизирует + можно задать параметры ВМ


    
