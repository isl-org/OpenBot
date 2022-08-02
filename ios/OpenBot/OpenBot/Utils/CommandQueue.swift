//
// Created by Sparsh Jain on 02/08/22.
//

import Foundation


// Command array, executed sequentially
class CommandQueue<Element> {
    var executeHandler: ((_ command: Element) -> Void)?

    private var queueLock = NSLock()

    /*
    private var queue = [Element]() {
        didSet {
            queueLock.lock()
            var shouldExecute = false
            // Start executing the first command (if it was not already executing)
            let nextElement = queue.first
            if oldValue.isEmpty, nextElement != nil {
                shouldExecute = true
            }
            DLog("queue size: \(queue.count)")
            queueLock.unlock()

            if shouldExecute {
                self.executeHandler?(nextElement!)
            }
        }
    }

    func first() -> Element? {
        queueLock.lock(); defer { queueLock.unlock() }
        return queue.first
    }

    func append(_ element: Element) {
        queue.append(element)
    }

    func next() {
        guard !queue.isEmpty else { return }

        // Delete finished command and trigger next execution if needed
        queue.removeFirst()

        if let nextElement = queue.first {
            executeHandler?(nextElement)
        }
    }

    func removeAll() {
        DLog("queue removeAll")
        queue.removeAll()
    }
 */

    private var queue = [Element]()

    func first() -> Element? {
        queueLock.lock(); defer { queueLock.unlock() }
        //DLog("queue: \(queue) first: \(queue.first)")
        return queue.first
    }

    func executeNext() {
        queueLock.lock()
        guard !queue.isEmpty else { queueLock.unlock(); return }

        //DLog("queue remove finished: \(queue.first)")
        // Delete finished command and trigger next execution if needed
        queue.removeFirst()
        let nextElement = queue.first
        queueLock.unlock()

        if let nextElement = nextElement {
            //DLog("execute next")
            executeHandler?(nextElement)
        }
    }

    func append(_ element: Element) {
        queueLock.lock()
        let shouldExecute = queue.isEmpty
        queue.append(element)
        queueLock.unlock()
        //DLog("queue: \(queue) append: \(element). total: \(queue.count)")

        if shouldExecute {
            executeHandler?(element)
        }
    }

    func removeAll() {
        // DLog("queue removeAll: \(queue.count)")
        queue.removeAll()
    }
}