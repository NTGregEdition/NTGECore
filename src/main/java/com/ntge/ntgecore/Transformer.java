package com.ntge.ntgecore;

import net.minecraft.launchwrapper.IClassTransformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class Transformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) {
            return null;
        }
        if (!"net.minecraft.client.Minecraft".equals(transformedName)) {
            return basicClass;
        }
        return patch(basicClass);
    }

    private byte[] patch(byte[] basicClass) {
        ClassReader classReader = new ClassReader(basicClass);
        ClassNode classNode = new ClassNode();
        classReader.accept(classNode, 0);

        boolean changed = false;

        for (Object methodObject : classNode.methods) {
            MethodNode methodNode = (MethodNode) methodObject;
            InsnList instructions = methodNode.instructions;
            AbstractInsnNode[] nodes = instructions.toArray();

            for (AbstractInsnNode insnNode : nodes) {
                if (!(insnNode instanceof MethodInsnNode)) {
                    continue;
                }

                MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;

                if ("org/lwjgl/opengl/Display".equals(methodInsnNode.owner) && "setTitle".equals(methodInsnNode.name)
                    && "(Ljava/lang/String;)V".equals(methodInsnNode.desc)) {
                    InsnList patch = new InsnList();
                    patch.add(new InsnNode(Opcodes.POP));
                    patch.add(new LdcInsnNode(Config.WINDOW_TITLE));
                    instructions.insertBefore(methodInsnNode, patch);
                    changed = true;
                }

                if ("org/lwjgl/opengl/Display".equals(methodInsnNode.owner) && "setIcon".equals(methodInsnNode.name)
                    && "([Ljava/nio/ByteBuffer;)I".equals(methodInsnNode.desc)) {
                    InsnList patch = new InsnList();
                    patch.add(new InsnNode(Opcodes.POP));
                    patch.add(
                        new MethodInsnNode(
                            Opcodes.INVOKESTATIC,
                            "com/ntge/ntgecore/IconHook",
                            "getIconBuffers",
                            "()[Ljava/nio/ByteBuffer;"));
                    instructions.insertBefore(methodInsnNode, patch);
                    changed = true;
                }
            }
        }

        if (!changed) {
            return basicClass;
        }

        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
