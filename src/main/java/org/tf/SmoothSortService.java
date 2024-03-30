package org.tf;

import java.util.ArrayList;
import java.util.List;

public class SmoothSortService {

    private long operations;
    private long before;
    private long after;

    public void checkAndSort(int[] array) {
        before = System.nanoTime();
        sort(array);
        after = System.nanoTime();
    }

    public void sort(int[] array) {
        sortLocal(array, array.length);
    }

    public void sort(int[] array, int bound) {
        sortLocal(array, bound);
    }

    // Текущая куча - самая правая куча
    private void sortLocal(int[] array, int bound) {
        if (array.length > 1) { // В других случаях массив остаётся в первоначальном виде
            List<Integer> leonardoNumbers = generateLeonardoNumbers(array);
            Node root1 = new Node(array[0]); // Корень "предправой" кучи (второй справа)
            Node root2 = new Node(array[1]); // Корень "правой" кучи (первый справа)
            operations += 2;
            root2.setPrevRootOfHeap(root1); // Делаем связь между двумя кучами, чтобы не потерять
            for (int i = 2; i < array.length; i++) { // Начинаем с третьего элемента, т.к. два по умолчанию существуют
                Node node = new Node(array[i]); // Текущий узел
                if (root1 != null && (Math.abs(leonardoNumbers.indexOf(root1.getSizeOfHeap()) -
                        leonardoNumbers.lastIndexOf(root2.getSizeOfHeap())) == 1)) {
                    // Если существует минимум 2 кучи и элементы(в качестве размеров куч) в массиве чисел Леонардо соседние, то...
                    node.setSizeOfHeap(root1.getSizeOfHeap() + root2.getSizeOfHeap() + 1); // Меняем размер кучи, где корнем является node
                    node.setLeft(root1); // Слева "предправая" куча
                    node.setRight(root2); // Справа правая куча => после этих действий у нас появилась общая куча из этих двух
                    if (root1.getPrevRootOfHeap() != null) { // Если есть кучи помимо этих двух, то...
                        node.setPrevRootOfHeap(root1.getPrevRootOfHeap()); // Переставляем указатель, направленный на ближайшую левую кучу, на созданную кучу
                        root1 = root1.getPrevRootOfHeap(); // Ту, на которую был указатель, представляем как вторую справа
                    }
                    else root1 = null; // Т.к. нет других куч
                    siftInHeap(node); // Просейка
                }
                else { // Иначе...
                    root1 = root2; // Если существовали оба корня, то смещаемся к самому "свежему" (предварительно создав связь, чтобы не потерять кучи)
                    node.setPrevRootOfHeap(root1); // Создаём связь между кучами
                }
                operations++;
                root2 = node; // Представляем нашу кучу как самую первую справа
            }
            setArray(array, root2, bound);
        }
    }

    private void setArray(int[] array, Node root, int bound) {
        Node currentRoot = root;
        for (int i = array.length - 1; i >= array.length - bound; i--) {
            array[i] = getHigh(currentRoot).getValue();
            operations++;
            if (currentRoot.getRight() != null) currentRoot = currentRoot.getRight();
            else currentRoot = currentRoot.getPrevRootOfHeap();
        }
        for (int i = array.length - bound - 1; i >= 0; i--) {
            array[i] = currentRoot.getValue();
            if (currentRoot.getRight() != null) currentRoot = currentRoot.getRight();
            else currentRoot = currentRoot.getPrevRootOfHeap();
        }
    }
    private Node getHigh(Node root) {
        siftBetweenHeaps(root, root, root); // Просейка
        return root;
    }

    private Node siftBetweenHeaps(Node root, Node current, Node result) { // 17 <- 16 <- 3
        Node prevRoot = current.getPrevRootOfHeap();
        if (prevRoot != null) {
            if (prevRoot.getValue() > result.getValue()) {
                result = prevRoot;
                operations++;
            }
            return siftBetweenHeaps(root, prevRoot, result);
        }
        if (root != result) {
            swapValues(root, result);
            siftInHeap(result);
        }
        return root;
    }

    private Node siftInHeap(Node root) {
        if (root != null && root.getLeft() != null) {
            Node maxNode = null;
            if (root.getLeft().getValue() > root.getValue()
                    && root.getLeft().getValue() >= root.getRight().getValue()) {
                maxNode = root.getLeft();
                operations++;
            }
            else if (root.getRight().getValue() > root.getValue()
                    && root.getRight().getValue() >= root.getLeft().getValue()) {
                maxNode = root.getRight();
                operations++;
            }
            if (maxNode != null) {
                swapValues(maxNode, root);
                return siftInHeap(maxNode);
            }
        }
        return root; // Вернёт наименьший node
    }

    private void swapValues(Node n1, Node n2) {
        int temp = n2.getValue();
        n2.setValue(n1.getValue());
        n1.setValue(temp);
        operations++;
    }

    private List<Integer> generateLeonardoNumbers(int[] inputArray) {
        List<Integer> leonardoNumbers = new ArrayList<>();
        leonardoNumbers.add(1);
        leonardoNumbers.add(1);
        operations += 2;
        for (int i = 2; leonardoNumbers.get(i - 1) < inputArray.length; i++) {
            leonardoNumbers.add(leonardoNumbers.get(i - 1) + leonardoNumbers.get(i - 2) + 1);
            operations++;
        }
        return leonardoNumbers;
    }

    public long getOperations() {
        return operations;
    }

    public long getBefore() {
        return before;
    }

    public long getAfter() {
        return after;
    }

    public long getTime() {
        return getAfter() - getBefore();
    }
}
