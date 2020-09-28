/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto2;

import java.awt.Image;
import java.io.BufferedWriter;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

/**
 *
 * @author ArielBM
 */
public class Main extends javax.swing.JFrame {

    ArrayList<Vertice> misVertices;
    ArrayList<Arista> misAristas;
    ArrayList<String> verticesL;
    ArrayList<String> aristasL;

    int s, m, contador;

    boolean errores;

    public Main() {
        initComponents();

        jLabel1.setHorizontalAlignment(JLabel.CENTER);
        jLabel1.setVerticalAlignment(JLabel.CENTER);

        jLabel4.setHorizontalAlignment(JLabel.CENTER);
        jLabel4.setVerticalAlignment(JLabel.CENTER);

        verticesL = new ArrayList<>();
        aristasL = new ArrayList<>();
        misVertices = new ArrayList<>();
        misAristas = new ArrayList<>();

        s = m = contador = 0;

        errores = false;

        setIconImage(new ImageIcon(getClass().getResource("icon.jpg")).getImage());

    }

    private void parsearJson(String ruta) {

        JSONParser parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(ruta));

            JSONObject jsonObject = (JSONObject) obj;

            JSONObject grafo = (JSONObject) jsonObject.get("grafo");

            JSONObject vertices = (JSONObject) grafo.get("vertices");
            JSONObject aristas = (JSONObject) grafo.get("aristas");

            JSONArray vertice = (JSONArray) vertices.get("vertice");

            jTextArea2.append("LISTA DE VÉRTICES:\n");

            Iterator<String> iterator = vertice.iterator();
            while (iterator.hasNext()) {

                String tmp = iterator.next();
                System.out.println(tmp);
                jTextArea2.append("\t" + tmp + "\n");
                verticesL.add(tmp);

            }

            System.out.println("");
            jTextArea2.append("\n");
            jTextArea2.append("LISTA DE ARISTAS:\n");

            JSONArray arista = (JSONArray) aristas.get("arista");

            Iterator<JSONObject> it2 = arista.iterator();
            while (it2.hasNext()) {
                JSONObject tmp = (JSONObject) it2.next();

                System.out.println(tmp.get("inicio") + " -> " + tmp.get("fin"));
                jTextArea2.append("\t" + tmp.get("inicio") + " -> " + tmp.get("fin") + "\n");

                if (contador < 10) {

                    if (buscarVertice(tmp.get("inicio").toString()) == false || buscarVertice(tmp.get("fin").toString()) == false) {

                        jTextArea1.append("Error en la arista: \"" + tmp.get("inicio").toString() + " -> " + tmp.get("fin").toString() + "\" Debido a que uno se sus vértices no ha sido declarado.\nNo se graficará.\n\n");
                        errores = true;

                    } else {
                        aristasL.add(tmp.get("inicio") + " -> " + tmp.get("fin"));
                        misAristas.add(new Arista(tmp.get("inicio").toString(), tmp.get("fin").toString()));
                        contador++;
                    }
                } else {
                    jTextArea1.append("La arista: \"" + tmp.get("inicio") + " -> " + tmp.get("fin") + "\" no será graficada debído a que se llegó al número máximo de aristas soportadas.\n\n");
                    errores = true;
                }

            }
            jTextArea2.append("\n");

        } catch (FileNotFoundException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
            //manejo de error
        } catch (IOException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
            //manejo de error
        } catch (ParseException e) {

            JOptionPane.showMessageDialog(null, e.getMessage());
            //manejo de error
        }
    }

    private boolean buscarVertice(String nombre) {
        for (int k = 0; k < verticesL.size(); k++) {
            if (verticesL.get(k).equals(nombre) == true) {
                return true;
            }
        }

        return false;
    }

    private void generarGrafo() throws IOException, InterruptedException {

        String ruta = "src\\misGrafos";                                //Definimos la ruta del archivo
        String nombre = "archivo" + s + ".txt";                                   //Definimos el nombre del archivo
        File archivo = new File(ruta, nombre);                            //Se crea el archivo en la ruta y con el nombre especificado
        BufferedWriter bw = new BufferedWriter(new FileWriter(archivo)); //Se crea un objeto escritor que editará el documento

        bw.write("digraph G {"); //Con el método "write" del escritor comenzamos a editar el texto del documento
        //Todo grafo en graphviz inicia con "digraph" + "nombre del Grafo" en este caso el nombre es "G"
        //El cuerpo del grafo va entre llaves

        bw.newLine();
        bw.newLine();

        bw.write("  edge [\n"
                + "    arrowhead=\"none\"\n"
                + "  ];");

        for (int i = 0; i < verticesL.size(); i++) {

            bw.write(verticesL.get(i) + ";");
        }

        for (int j = 0; j < aristasL.size(); j++) {

            bw.write(aristasL.get(j) + ";");
        }

        bw.write("}"); //Cerramos el cuerpo del documento con otra llave
        bw.close();    //Cerramos el editor

        String ruta1 = "src\\misGrafos\\archivo" + s + ".txt";       //Ruta donde se encuentra el archivo txt
        String ruta2 = "src\\misGrafos\\archivo" + s + ".png";       //Ruta donde se creará la imagen
        String cmd = "dot -Tpng " + ruta1 + " -o " + ruta2; //Comando que será ejecutado por el CMD
        Process exec = Runtime.getRuntime().exec(cmd);      //Ejecución del comando por el CMD

        Thread.sleep(500);

        ImageIcon imagen = new ImageIcon("src\\misGrafos\\archivo" + s + ".png");
        jLabel1.setIcon(imagen);
        s++;
    }

    private void generarMatriz() {

        if (verticesL.size() > 0) {

            for (int i = 0; i < verticesL.size(); i++) {
                misVertices.add(new Vertice(verticesL.get(i)));
            }

            for (int i = 0; i < misVertices.size(); i++) {
                for (int j = 0; j < misVertices.size(); j++) {
                    misVertices.get(i).getConexiones().add(new Adyacencia(misVertices.get(j).getNombre()));
                }
            }

            for (int i = 0; i < misAristas.size(); i++) {
                agregarAdyacencia(misAristas.get(i).getInicio(), misAristas.get(i).getFin());
            }

            try {

                String ruta = "src\\misGrafos";                                //Definimos la ruta del archivo
                String nombre = "matriz" + m + ".txt";                                   //Definimos el nombre del archivo
                File archivo = new File(ruta, nombre);                            //Se crea el archivo en la ruta y con el nombre especificado
                BufferedWriter bw = new BufferedWriter(new FileWriter(archivo)); //Se crea un objeto escritor que editará el documento

                bw.write("digraph G {"); //Con el método "write" del escritor comenzamos a editar el texto del documento
                bw.newLine(); //Todo grafo en graphviz inicia con "digraph" + "nombre del Grafo" en este caso el nombre es "G"
                bw.newLine(); //El cuerpo del grafo va entre llaves

                bw.write("node [shape=record];");
                bw.newLine();
                bw.newLine();

                bw.write("subgraph cluster_0{");
                bw.newLine();
                bw.newLine();

                bw.write("label = \"Matriz de Adyacencia\";");
                bw.newLine();
                bw.newLine();

                bw.write("struct3 [label=\"{|");

                for (int i = 0; i < verticesL.size(); i++) {

                    bw.write(verticesL.get(i));

                    if (i != (verticesL.size() - 1)) {
                        bw.write("|");
                    }
                }

                bw.write("}");

                for (int i = 0; i < misVertices.size(); i++) {
                    Vertice tmp = misVertices.get(i);
                    bw.write("|{" + tmp.getNombre());

                    for (int j = 0; j < tmp.getConexiones().size(); j++) {
                        bw.write("|" + tmp.getConexiones().get(j).getCantidad());
                    }

                    bw.write("}");
                }

                bw.write("\"];");
                bw.newLine();
                bw.newLine();

                bw.write("color = black;");
                bw.newLine();
                bw.newLine();

                bw.write("}");
                bw.newLine();
                bw.newLine();

                bw.write("}"); //Cerramos el cuerpo del documento con otra llave
                bw.close();    //Cerramos el editor

                String ruta1 = "src\\misGrafos\\matriz" + m + ".txt";       //Ruta donde se encuentra el archivo txt
                String ruta2 = "src\\misGrafos\\matriz" + m + ".png";       //Ruta donde se creará la imagen
                String cmd = "dot -Tpng " + ruta1 + " -o " + ruta2; //Comando que será ejecutado por el CMD
                Process exec = Runtime.getRuntime().exec(cmd);      //Ejecución del comando por el CMD

                Thread.sleep(500);

                ImageIcon imagen = new ImageIcon("src\\misGrafos\\matriz" + m + ".png");
                jLabel4.setIcon(imagen);
                m++;

            } catch (Exception e) {
            }
        }
    }

    private void agregarAdyacencia(String v1, String v2) {

        Vertice actual;

        for (int i = 0; i < misVertices.size(); i++) {

            actual = misVertices.get(i);

            if (actual.getNombre().equals(v1) == true) {

                for (int j = 0; j < misVertices.size(); j++) {
                    if (actual.getConexiones().get(j).getVecino().equals(v2) == true) {
                        actual.getConexiones().get(j).setCantidad(actual.getConexiones().get(j).getCantidad() + 1);
                        break;

                    }
                }

                break;
            }
        }

        if (v1.equals(v2) == false) {
            for (int i = 0; i < misVertices.size(); i++) {

                actual = misVertices.get(i);

                if (actual.getNombre().equals(v2) == true) {

                    for (int j = 0; j < misVertices.size(); j++) {
                        if (actual.getConexiones().get(j).getVecino().equals(v1) == true) {
                            actual.getConexiones().get(j).setCantidad(actual.getConexiones().get(j).getCantidad() + 1);
                            break;

                        }
                    }

                    break;
                }
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton3 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Proyecto_2");
        setResizable(false);

        jButton3.setText("Leer Archivo");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jTextArea2.setEditable(false);
        jTextArea2.setColumns(20);
        jTextArea2.setFont(new java.awt.Font("Rockwell", 0, 18)); // NOI18N
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jButton1.setText("Salir");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jScrollPane1.setBackground(new java.awt.Color(255, 51, 255));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setOpaque(true);
        jScrollPane1.setViewportView(jLabel1);

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setOpaque(true);
        jScrollPane3.setViewportView(jLabel4);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Rockwell", 0, 18)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane4.setViewportView(jTextArea1);

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Errores En El Archivo:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel3.setText("Matriz de Adyacencia");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jScrollPane4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jScrollPane3)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String ruta;
        JFileChooser file = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON FILES", "json", "json");
        file.setFileFilter(filter);
        file.showOpenDialog(this);

        if (file.getSelectedFile() != null) {

            contador = 0;
            errores = false;

            jTextArea1.setText("");
            jTextArea2.setText("");
            verticesL.clear();
            aristasL.clear();
            misAristas.clear();
            misVertices.clear();

            ruta = file.getSelectedFile().getAbsolutePath();
            parsearJson(ruta);

            try {
                Thread.sleep(200);
                generarGrafo();
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }

            generarMatriz();

            if (!errores) {
                jTextArea1.setText("No se detectaron errores en el archivo seleccionado :)\n");
            }

        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        System.exit(0);

    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    // End of variables declaration//GEN-END:variables
}
