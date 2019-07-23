/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.bstguesser;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import static java.lang.Math.max;

public class TreeNode {
    private static final int SIZE = 60;
    private static final int MARGIN = 20;
    private int value, height;
    protected TreeNode left, right;
    private boolean showValue;
    private int x, y;
    private int color = Color.rgb(150, 150, 250);

    public TreeNode(int value) {
        this.value = value;
        this.height = 0;
        showValue = false;
        left = null;
        right = null;
    }

    public void insert(int valueToInsert) {

        //Move To RIght Subtree
        if(value<valueToInsert)
        {
            if(right==null)
            {
                TreeNode node=new TreeNode(valueToInsert);
                node.left=null;
                node.right=null;
                this.right=node;

            }
            else
            {
                right.insert(valueToInsert);
            }
        }
        //Move To Left Subtree
        else if(value>valueToInsert)
        {
            if(left==null)
            {
                TreeNode node=new TreeNode(valueToInsert);
                node.left=null;
                node.right=null;
                this.left=node;
            }
            else
            {
                left.insert(valueToInsert);
            }
        }
        else
            return;

        height=treeHeight();

        Log.d("AVL","Value : "+value +" Height: "+height);

        int bal=balance();

        Log.d("AVL-Balance","Value : "+bal);

        //reArrangeTree(bal);

    }

    public void reArrangeTree(int bal)
    {
        if(bal<-1)
        {
            leftRotate();
        }

        else if(bal>1)
        {
            rightRotate();
        }

        else if(bal==1 && right==null)
        {
            rightRotate();
        }

        else if(bal==-1 && left==null)
        {
            leftRotate();
        }

    }

    public int balance()
    {
        if(left==null && right==null)
            return 0;

        else if(left==null && right!=null)
            return (0-right.height);

        else if(right==null && left!=null)
            return (left.height-0);

        else
            return (left.height-right.height);

    }

    public int treeHeight()
    {
        if(left==null && right==null)
            return 0;

        else if(left==null && right!=null)
            return right.height++;

        else if(right==null && left!=null)
            return left.height++;

        else
        {
            height=1+max(left.height,right.height);
            return height;
        }
    }

    public void rightRotate()
    {
        int temp=value;
        value=left.value;
        left.left.value=temp;

        right=left;
        left=right.left;
        right.left=null;

        height=treeHeight();
    }

    public void leftRotate()
    {
        int temp=value;
        value=right.value;
        right.right.value=temp;

        left=right;
        right=left.right;
        left.right=null;

        height=treeHeight();

    }


    public int getValue() {
        return value;
    }

    public void positionSelf(int x0, int x1, int y) {
        this.y = y;
        x = (x0 + x1) / 2;

        if(left != null) {
            left.positionSelf(x0, right == null ? x1 - 2 * MARGIN : x, y + SIZE + MARGIN);
        }
        if (right != null) {
            right.positionSelf(left == null ? x0 + 2 * MARGIN : x, x1, y + SIZE + MARGIN);
        }
    }

    public void draw(Canvas c) {
        Paint linePaint = new Paint();
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.GRAY);
        if (left != null)
            c.drawLine(x, y + SIZE/2, left.x, left.y + SIZE/2, linePaint);
        if (right != null)
            c.drawLine(x, y + SIZE/2, right.x, right.y + SIZE/2, linePaint);

        Paint fillPaint = new Paint();
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setColor(color);
        c.drawRect(x-SIZE/2, y, x+SIZE/2, y+SIZE, fillPaint);

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(SIZE * 2/3);
        paint.setTextAlign(Paint.Align.CENTER);
        c.drawText(showValue ? String.valueOf(value) : "?", x, y + SIZE * 3/4, paint);

        if (height > 0) {
            Paint heightPaint = new Paint();
            heightPaint.setColor(Color.MAGENTA);
            heightPaint.setTextSize(SIZE * 2 / 3);
            heightPaint.setTextAlign(Paint.Align.LEFT);
            c.drawText(String.valueOf(height), x + SIZE / 2 + 10, y + SIZE * 3 / 4, heightPaint);
        }

        if (left != null)
            left.draw(c);
        if (right != null)
            right.draw(c);
    }

    public int click(float clickX, float clickY, int target) {
        int hit = -1;
        if (Math.abs(x - clickX) <= (SIZE / 2) && y <= clickY && clickY <= y + SIZE) {
            if (!showValue) {
                if (target != value) {
                    color = Color.RED;
                } else {
                    color = Color.GREEN;
                }
            }
            showValue = true;
            hit = value;
        }
        if (left != null && hit == -1)
            hit = left.click(clickX, clickY, target);
        if (right != null && hit == -1)
            hit = right.click(clickX, clickY, target);
        return hit;
    }

    public void invalidate() {
        color = Color.CYAN;
        showValue = true;
    }
}
