package br.octahedron.cotopaxi.cloudservice;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A distributed <b>reentrant lock</b> (semaphore) over memcache. It means that, once acquired the
 * lock, another attempt to call lock the same resource will be successful. By the same logical, try
 * to unlock a resource previously unlocked has no effects.
 * 
 * @see {@link Lock}
 * @see {@link ReentrantLock}
 * 
 * @author Danilo Queiroz - daniloqueiroz@octahedron.com.br
 * @author Vítor Avelino - vitoravelino@octahedron.com.br
 * 
 */
public interface DistributedLock {

	/**
	 * Tries to obtain the resource lock immediately. If the lock isn't free, return without the
	 * lock.
	 * 
	 * @return <code>true</code> if the lock was acquired, <code>false</code> otherwise.
	 */
	public abstract boolean tryLock();

	/**
	 * Tries to obtain the resource lock. If the lock isn't free, it waits for. If the method
	 * returns normally the lock was acquired. However, this method can throws a
	 * <code>TimeoutException</code> if its wait to long, avoiding deadlocks.
	 * 
	 * @throws TimeoutException
	 *             If takes to long to acquire the lock. The timeout is implementation dependent.
	 */
	public abstract void lock() throws TimeoutException;

	/**
	 * Unlocks the resource. If the thread doesn't have the lock, nothing is done.
	 */
	public abstract void unlock();

	/**
	 * Gets this lock's name
	 * 
	 * @return the lock's name
	 */
	public abstract String getName();

}