package cn.knqiufan.rpc.core.governance;

import cn.knqiufan.rpc.core.consumer.KnInvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 滑动时间窗口
 *
 * @author knqiufan
 * @version 1.0.0
 * @date 2024/4/9 0:48
 */
public class SlidingTimeWindow {
  private static final Logger log = LoggerFactory.getLogger(KnInvocationHandler.class);

  public static final int DEFAULT_SIZE = 30;

  private final int size;
  private final RingBuffer ringBuffer;
  private int sum = 0;

  //    private int _start_mark = -1;
//    private int _prev_mark  = -1;
  private int _curr_mark  = -1;

  private long _start_ts = -1L;
  //   private long _prev_ts  = -1L;
  private long _curr_ts  = -1L;

  public SlidingTimeWindow() {
    this(DEFAULT_SIZE);
  }

  public SlidingTimeWindow(int _size) {
    this.size = _size;
    this.ringBuffer = new RingBuffer(this.size);
  }

  /**
   * record current ts millis.
   *
   * @param millis
   */
  public synchronized void record(long millis) {
    log.debug("window before: " + this.toString());
    log.debug("window.record(" + millis + ")");
    long ts = millis / 1000;
    if (_start_ts == -1L) {
      initRing(ts);
    } else {   // TODO  Prev 是否需要考虑
      if(ts == _curr_ts) {
        log.debug("window ts:" + ts + ", curr_ts:" + _curr_ts + ", size:" + size);
        this.ringBuffer.incr(_curr_mark, 1);
      } else if(ts > _curr_ts && ts < _curr_ts + size) {
        int offset = (int)(ts - _curr_ts);
        log.debug("window ts:" + ts + ", curr_ts:" + _curr_ts + ", size:" + size + ", offset:" + offset);
        this.ringBuffer.reset(_curr_mark + 1, offset);
        this.ringBuffer.incr(_curr_mark + offset, 1);
        _curr_ts = ts;
        _curr_mark = (_curr_mark + offset) % size;
      } else if(ts >= _curr_ts + size) {
        log.debug("window ts:" + ts + ", curr_ts:" + _curr_ts + ", size:" + size);
        this.ringBuffer.reset();
        initRing(ts);
      }
    }
    this.sum = this.ringBuffer.sum();
    log.debug("window after: " + this.toString());
  }

  private void initRing(long ts) {
    log.debug("window initRing ts:" + ts);
    this._start_ts  = ts;
    this._curr_ts   = ts;
    this._curr_mark = 0;
    this.ringBuffer.incr(0, 1);
  }

  public int getSize() {
    return size;
  }

  public int getSum() {
    return sum;
  }

  public RingBuffer getRingBuffer() {
    return ringBuffer;
  }

  public int get_curr_mark() {
    return _curr_mark;
  }

  public long get_start_ts() {
    return _start_ts;
  }

  public long get_curr_ts() {
    return _curr_ts;
  }

  @Override
  public String toString() {
    return "SlidingTimeWindow{" +
            "size=" + size +
            ", ringBuffer=" + ringBuffer +
            ", sum=" + sum +
            ", _curr_mark=" + _curr_mark +
            ", _start_ts=" + _start_ts +
            ", _curr_ts=" + _curr_ts +
            '}';
  }
}
