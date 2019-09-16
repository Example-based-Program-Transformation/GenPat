package org.eclipse.swt.examples.paint;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */


public class PaintSurface {
	private Canvas       paintCanvas;
	public PaintSurface(Canvas paintCanvas, PaintStatus paintStatus) {
	 * Returns the display Graphics Context associated with this surface.
	 * 
	 * @return the display GC associated with this surface
	 */
	public GC getDisplayGC() {
		return displayGC;
	}

	 * Sets the current paint session.
	 * If oldPaintSession != paintSession calls oldPaintSession.end()
	 * @param paintSession the paint session to activate; null to disable all sessions
	 */
	public void setPaintSession(PaintSession paintSession) {
		if (this.paintSession != null) {
			this.paintSession.endSession();
		this.paintSession = paintSession;
		paintStatus.clear();

	/**
	 * Returns the current paint session.
	 * 
	 * @return the current paint session, null if none is active
	 */
	public PaintSession getPaintSession() {
		return paintSession;
	}

