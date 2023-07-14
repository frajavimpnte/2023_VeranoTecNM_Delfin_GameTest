package fjmp.game.framework.gl;

import javax.microedition.khronos.opengles.GL10;

import java.lang.Math;

import fjmp.game.framework.impl.GLGraphics;
import fjmp.game.framework.math.Vector2;

public class SpriteBatcher {
    final float[] vericesBuffer;
    int bufferIndex;
    final Vertices vertices;
    int numSprites;

    public SpriteBatcher(GLGraphics glGraphics, int maxSprites) {
        this.vericesBuffer = new float[maxSprites*4*4];
        this.vertices = new Vertices(glGraphics, maxSprites*4,
            maxSprites*6, false, true);
        this.bufferIndex = 0;
        this.numSprites = 0;

        short[] indices = new short[maxSprites*6];
        int len = indices.length;
        short j = 0;
        for (int i = 0; i < len; i += 6, j += 4) {
            indices[i + 0] = (short) (j + 0);
            indices[i + 1] = (short) (j + 1);
            indices[i + 2] = (short) (j + 2);
            indices[i + 3] = (short) (j + 2);
            indices[i + 4] = (short) (j + 3);
            indices[i + 5] = (short) (j + 0);
        }
        vertices.setIndices(indices, 0, indices.length);
    }

    public void beginBatch(Texture texture) {
        texture.bind();
        numSprites = 0;
        bufferIndex = 0;
    }

    public void endBatch() {
        vertices.setVertices(vericesBuffer, 0, bufferIndex);
        vertices.bind();
        vertices.draw(GL10.GL_TRIANGLES, 0, numSprites * 6);
        vertices.unbind();
    }

    public void drawSprite(float x, float y, float width, float height, TextureRegion region) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;
        float x1 = x - halfWidth;
        float y1 = y - halfHeight;
        float x2 = x + halfWidth;
        float y2 = y + halfHeight;

        vericesBuffer[bufferIndex++]  = x1;
        vericesBuffer[bufferIndex++]  = y1;
        vericesBuffer[bufferIndex++]  = region.u1;
        vericesBuffer[bufferIndex++]  = region.v2;

        vericesBuffer[bufferIndex++]  = x2;
        vericesBuffer[bufferIndex++]  = y1;
        vericesBuffer[bufferIndex++]  = region.u2;
        vericesBuffer[bufferIndex++]  = region.v2;

        vericesBuffer[bufferIndex++]  = x2;
        vericesBuffer[bufferIndex++]  = y2;
        vericesBuffer[bufferIndex++]  = region.u2;
        vericesBuffer[bufferIndex++]  = region.v1;

        vericesBuffer[bufferIndex++]  = x1;
        vericesBuffer[bufferIndex++]  = y2;
        vericesBuffer[bufferIndex++]  = region.u1;
        vericesBuffer[bufferIndex++]  = region.v1;

        numSprites++;
    }

    public void drawSprite(float x, float y, float width, float height, float angle,
        TextureRegion region) {
        float halfWidth = width / 2;
        float halfHeight = height / 2;

        float rad = angle * Vector2.TO_RADIANS;
        float cos = (float) Math.cos(rad);
        float sin = (float) Math.sin(rad);

        float x1 = -halfWidth * cos - (-halfHeight) * sin;
        float y1 = -halfWidth * sin + (-halfHeight) * cos;
        float x2 = halfWidth * cos - (-halfHeight) * sin;
        float y2 = halfWidth * sin + (-halfHeight) * cos;
        float x3 = halfWidth * cos - halfHeight * sin;
        float y3 = halfWidth * sin + halfHeight * cos;
        float x4 = -halfWidth * cos - halfHeight * sin;
        float y4 = -halfWidth * sin + halfHeight * cos;

        x1 += x;
        y1 += y;
        x2 += x;
        y2 += y;
        x3 += x;
        y3 += y;
        x4 += x;
        y4 += y;

        vericesBuffer[bufferIndex++]  = x1;
        vericesBuffer[bufferIndex++]  = y1;
        vericesBuffer[bufferIndex++]  = region.u1;
        vericesBuffer[bufferIndex++]  = region.v2;

        vericesBuffer[bufferIndex++]  = x2;
        vericesBuffer[bufferIndex++]  = y2;
        vericesBuffer[bufferIndex++]  = region.u2;
        vericesBuffer[bufferIndex++]  = region.v2;

        vericesBuffer[bufferIndex++]  = x3;
        vericesBuffer[bufferIndex++]  = y3;
        vericesBuffer[bufferIndex++]  = region.u2;
        vericesBuffer[bufferIndex++]  = region.v1;

        vericesBuffer[bufferIndex++]  = x4;
        vericesBuffer[bufferIndex++]  = y4;
        vericesBuffer[bufferIndex++]  = region.u1;
        vericesBuffer[bufferIndex++]  = region.v1;

        numSprites++;
    }
}
