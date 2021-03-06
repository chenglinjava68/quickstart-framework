/**
 * 项目名称：quickstart-disruptor 
 * 文件名：RingBuffer.java
 * 版本信息：
 * 日期：2018年9月25日
 * Copyright yangzl Corporation 2018
 * 版权所有 *
 */
package org.quickstart.disruptor.ring.buffer;

/**
 * RingBuffer 
 *  
 * @author：youngzil@163.com
 * @2018年9月25日 下午8:40:23 
 * @since 1.0
 */
public class RingBuffer<Item> {
    public Item[] a=null;
    public int writePos=0;
    public int readPos=0;
    public boolean flipped=false; //用来标记缓存区域是否已经被完整写入并读取过一轮，默认没有
    public RingBuffer(int cap){
        this.a=(Item[])new Object[cap];
    }
    public void reset(){
        this.writePos=0;
        this.readPos=0;
        this.flipped=false;
    }
    public int size(){
        return a.length;
    }
 
    public int available(){  //可读取区域，已经写入但还没有读取的区域
        if(!flipped){  //如果缓冲区还没有完全写入过一轮
            return writePos-readPos;
        }
        return size()-readPos+writePos; //如果已经完整读取过一轮，size()-readPos为负，readPos已经是第二轮的位置，所以加上writePos是第二轮已写入未读取区域
    }
 
    public int remainingCapacity(){  //剩余还没写入的容量
        if(!flipped){  //如果没有被完整写入过一轮
            return size()-writePos;
        }
        return readPos-writePos; //已经完整读过一轮，这里readPos是第二轮的位置，相当于size()+第一轮的readPos，减去writPos相当于完全没有被写入过的区域加上第二轮已经被读过可以被覆盖写入的区域
    }
    public boolean put(Item x){
        if(!flipped){  //如果没有被完整写入过一轮
            if(writePos==size()){  //一轮缓存已经全部写满
                writePos=0;  //第一轮写满了，写入标签重置到缓冲区开头位置
                flipped=true;  //第一轮已经完全写过了
                if(writePos<readPos){  //缓冲区已经开始被读取了一部分了
                    a[writePos++]=x;  //第一轮的写入缓存已经被读过，可以覆盖写入
                    return true;
                }else{
                    return false; //缓存一轮已满还没被读过，就不写入
                }
            }else{  //缓冲区第一轮还没有写满，可以继续写入
                a[writePos++]=x;
                return true;
            }
        }else{  //已经完整写入过了一轮
            if(writePos<readPos){  //上一轮缓存已经读了一部分
                a[writePos++]=x;   //可以覆盖写入
                return true;
            }else{   //完整写入了一轮但还没有读过，那就不写入
                return false;
            }
        }
    }
 
    public Item take(){
        if(!flipped){  //没有完整写入过一轮
            if(readPos<writePos){
                return a[readPos++];  //在没有读取完写入部分时可以读取
            }else{
                return null;
            }
        }else{  //已经完整写入过一轮
            if(readPos==size()){  //在之前的缓存中已经读到缓冲区结尾，已经读完了一轮
                readPos=0;   //重置到初始位置开始读取新一轮写入的缓存
                flipped=false;  //对于新一轮写入的缓存来说，确实没有读取过
                if(readPos<writePos){  //没有完全读完写入的部分
                    return a[readPos++];
                }else{
                    return null;
                }
            }else{  //还没有读完之前的缓存，一轮没有读完
                return a[readPos++];  //继续读取原来一轮的缓存
            }
        }
    }
 
    public static void main(String[] args){
        int capacity=10;
        RingBuffer<String> ringBuffer=new RingBuffer<String>(capacity);
        /*
        * 写入
        * */
        for(int i=0;i<capacity;i++){
            String inputItem=i+"";
            boolean putSuccess=ringBuffer.put(inputItem);
            System.out.println(putSuccess?"插入"+inputItem+"成功":"插入"+inputItem+"失败");
        }
        /*
        * 下一轮写入与读取
        * */
        for(int i=0;i<capacity+1;i++){
            if(i==capacity-1){
                String takeItem=ringBuffer.take();
                System.out.println("取出"+takeItem+"成功");
            }
            if(i==capacity){
                String takeItem=ringBuffer.take();
                System.out.println("取出"+takeItem+"成功");
            }
            String inputItem=i+"";
            boolean putSuccess=ringBuffer.put(inputItem);
            System.out.println(putSuccess?"插入"+inputItem+"成功":"插入"+inputItem+"失败");  //只有在i=9和10的时候才会各读取一次，所以这里的插入大部分都会失败，因为前面写入一轮缓冲区已经满了
        }
    }
}
