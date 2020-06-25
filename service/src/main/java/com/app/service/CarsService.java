package com.app.service;

import com.app.converter.json.CarsJsonConverter;
import com.app.converter.model.Car;
import com.app.converter.model.enums.Color;
import com.app.service.enums.SortItem;
import com.app.service.exception.CarsServiceException;
import com.app.service.validator.CarValidator;
import org.eclipse.collections.impl.collector.Collectors2;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CarsService {
    private final Set<Car> cars;

    public CarsService(String filename) {
        cars = init(filename);
    }

    private Set<Car> init(String filename) {
        AtomicInteger counter = new AtomicInteger(1);
        CarValidator carValidator = new CarValidator();
        return new CarsJsonConverter(filename)
                .fromJson()
                .orElseThrow(() -> new CarsServiceException("json conversion has been failed"))
                .stream()
                .filter(car -> {
                    var errors = carValidator.validate(car);
                    if (!errors.isEmpty()) {
                        var errorMessage = errors
                                .entrySet()
                                .stream()
                                .map(e -> e.getKey() + ": " + e.getValue())
                                .collect(Collectors.joining(", "));
                        System.out.println("--------> VALIDATION ERROR FOR CAR NO. " + counter.get());
                        System.out.println(errorMessage);
                        carValidator.getErrors().clear();
                    }
                    counter.getAndIncrement();
                    return errors.isEmpty();
                }).collect(Collectors.toSet());
    }

    @Override
    public String toString() {
        return cars
                .stream()
                .map(Car::toString)
                .collect(Collectors.joining("\n"));
    }

    public List<Car> sortBy(SortItem sortItem, boolean reverse) {

        if (sortItem == null) throw new IllegalArgumentException("Sort item is null!");
        Stream<Car> sortedCars = switch (sortItem) {
            case COLOR -> cars.stream().sorted(Comparator.comparing(Car::getColor));
            case MILEAGE -> cars.stream().sorted(Comparator.comparing(Car::getMileage));
            case MODEL -> cars.stream().sorted(Comparator.comparing(Car::getModel));
            case PRICE -> cars.stream().sorted(Comparator.comparing(Car::getPrice));
        };

        List<Car> cars = sortedCars.collect(Collectors.toList());
        if (reverse) Collections.reverse(cars);
        return cars;
    }

    public List<Car> withLargestMileage(double mileage) {
        if (mileage <= 0) throw new CarsServiceException("Mileage value have to be more than zero!");
        return cars
                .stream()
                .filter(x -> x.getMileage() > mileage)
                .collect(Collectors.toList());
    }

    public Map<Color, Long> colorRepetitions() {
        return this.cars
                .stream()
                .collect(Collectors.groupingBy(Car::getColor, Collectors.counting()))
                .entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public Map<String, Car> biggestPrice() {
        return this.cars
                .stream()
                .collect(Collectors.groupingBy(
                        Car::getModel,
                        Collectors.collectingAndThen(Collectors.maxBy(Comparator.comparing(Car::getPrice)), Optional::orElseThrow)
                ));

    }

    public CarStatistics statistics() {
        DoubleSummaryStatistics mileageSummary = this.cars
                .stream()
                .mapToDouble(Car::getMileage)
                .summaryStatistics();

        Statistic mileageStatistic = new Statistic(
                BigDecimal.valueOf(mileageSummary.getMin()),
                BigDecimal.valueOf(mileageSummary.getMax()),
                BigDecimal.valueOf(mileageSummary.getAverage()));

        var priceSummary = cars.stream().map(Car::getPrice).collect(Collectors2.summarizingBigDecimal(price -> price));
        Statistic priceStatistics = new Statistic(priceSummary.getMin(), priceSummary.getMax(), priceSummary.getAverage());

        return new CarStatistics(mileageStatistic, priceStatistics);
    }

    public List<Car> mostExpensive() {
        return
                cars
                        .stream()
                        .collect(Collectors.groupingBy(Car::getPrice))
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByKey())
                        .orElseThrow(() -> new CarsServiceException("Cars list is empty!"))
                        .getValue();
    }

    public List<Car> sortComponentsList() {
        return this.cars
                .stream()
                .peek(car -> car.setComponents(car.getComponents().stream().sorted().collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    public Map<String, List<Car>> componentsWithCarList() {

        return sortByValueElements(cars
                .stream()
                .flatMap(car -> car.getComponents().stream())
                .distinct()
                .collect(Collectors.toMap(
                        component -> component,
                        component -> cars
                                .stream()
                                .filter(car -> car
                                        .getComponents()
                                        .contains(component)).collect(Collectors.toList()))));

    }

    private Map<String, List<Car>> sortByValueElements(Map<String, List<Car>> components) {
        if (components == null) {
            throw new IllegalArgumentException("Map is null!");
        }
        List<Map.Entry<String, List<Car>>> entryComponents = new LinkedList<>(components.entrySet());
        Collections.sort(entryComponents, Comparator.comparingInt(o -> o.getValue().size()));
        Map<String, List<Car>> sortedMap = new TreeMap<>();
        entryComponents.forEach(x -> sortedMap.put(x.getKey(), x.getValue()));

        return sortedMap;
    }

    public List<Car> priceInRange(BigDecimal min, BigDecimal max) {
        if (Objects.isNull(min) || Objects.isNull(max)) {
            throw new CarsServiceException("Price value is null!");
        }
        if (min.compareTo(max) > 0) {
            throw new CarsServiceException("Price can not be less than zero!");
        }
        return this.cars
                .stream()
                .filter(x -> x.getPrice().compareTo(min) >= 0 && x.getPrice().compareTo(max) <= 0)
                .collect(Collectors.toList());
    }

}
